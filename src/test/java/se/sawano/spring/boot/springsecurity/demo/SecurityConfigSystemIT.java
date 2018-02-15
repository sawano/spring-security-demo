/*
 * Copyright 2018 Daniel Sawano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.sawano.spring.boot.springsecurity.demo;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.FOUND;
import static se.sawano.spring.boot.springsecurity.demo.CommonSecurityAssertions.assertCommonSecurityHeaders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
//@ActiveProfiles("with-ssl")
public final class SecurityConfigSystemIT {

    @Autowired
    TestRestTemplate restTemplate;
    private HttpHeaders headers;

    @Test
    public void should_respond_with_required_security_headers() throws Exception {

        whenRequestingStartPage();

        assertCommonSecurityHeaders(this::getHeaderValue);

        thenHeader("Strict-Transport-Security").hasValue("max-age=31536000 ; includeSubDomains"); // HSTS

    }

    void whenRequestingStartPage() {
        final ResponseEntity<String> response = restTemplate.exchange("/", GET, httpEntityThatSimulatesSSLTerminatingProxy(), String.class);

        assertThat(response.getStatusCode(), is(FOUND));
        headers = response.getHeaders();
        printHeaders();
    }

    static HttpEntity<Object> httpEntityThatSimulatesSSLTerminatingProxy() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-forwarded-proto", "https");
        headers.add("X-forwarded-for", "127.0.0.1");
        return new HttpEntity<>(headers);
    }

    private void printHeaders() {
        System.out.println("--------------------------");
        System.out.println("Response headers:");
        headers.forEach((key, value) -> System.out.println(key + " -> " + value));
        System.out.println("--------------------------");
    }

    private ExpectedHeader thenHeader(final String headerName) {
        return new ExpectedHeader(headerName);
    }

    final class ExpectedHeader {
        final String headerName;

        public ExpectedHeader(final String headerName) {
            this.headerName = headerName;
        }

        public void hasValue(final String value) throws Exception {
            final List<String> actual = headers.get(headerName);
            assertThat(actual.size(), is(1));
            assertThat(actual.get(0), is(value));
        }

    }

    String getHeaderValue(final String s) {
        final List<String> actual = headers.get(s);
        assertThat(actual.size(), is(1));
        return actual.get(0);
    }

    //    @Configuration
    static class Conf {
        @Bean
        public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {

            // TODO take this from yaml
            final String password = "spring-security-demo";
            SSLContext sslContext = SSLContextBuilder
                    .create()
                    .loadKeyMaterial(ResourceUtils.getFile("classpath:keystore.jks"), password.toCharArray(), password.toCharArray())
                    .loadTrustMaterial(ResourceUtils.getFile("classpath:truststore.jks"), password.toCharArray())
                    .build();

            HttpClient client = HttpClients.custom()
                                           .setSSLContext(sslContext)
                                           .build();

            return builder
                    .requestFactory(new HttpComponentsClientHttpRequestFactory(client))
                    .build();
        }
    }
}
