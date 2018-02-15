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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static java.time.Duration.ofDays;
import static org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String EXAMPLE_CSP = "script-src 'self' https://safescripts.example.com; object-src https://safeplugins.example.com; report-uri /csp-report-endpoint/";

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
            .antMatchers("/login*", "/webjars/**", "**/favicon.ico", "/css/**", "/js/**", "/images/**").anonymous()
            .anyRequest().authenticated()

            .and()
            // We specify customize urls to "hide" that we're using Spring Security
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .failureUrl("/login?error=true")
            .permitAll()

            .and()
            .logout().logoutSuccessUrl("/")

            .and()

            .headers()
            .referrerPolicy(SAME_ORIGIN)

            .and()
            .httpStrictTransportSecurity().maxAgeInSeconds(ofDays(365).getSeconds())

            .and()
            .contentSecurityPolicy(EXAMPLE_CSP)

            .and() // HTTP Public Key Pinning (HPKP)
            .httpPublicKeyPinning()
            .includeSubDomains(true)
            .reportUri("http://example.com/pkp-report")
            .addSha256Pins("d6qzRu9zOECb90Uez27xWltNsj0e1Md7GkYYkVoZWmM=", "E9CZ9INDbd+2eRQozYqqbQ2yXLVKB9+xcprMF+44U1g=");


    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("john").password("doe").roles("USER");
    }
}
