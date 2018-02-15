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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.sawano.spring.boot.springsecurity.demo.CommonSecurityAssertions.assertCommonSecurityHeaders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public final class SecurityConfigIT {

    final String JOHN = "john"; // As found in se.sawano.spring.boot.springsecurity.demo.SecurityConfiguration
    final String JOHN_PASSWORD = "doe"; // As found in se.sawano.spring.boot.springsecurity.demo.SecurityConfiguration

    @Autowired
    MockMvc mvc;
    ResultActions resultActions;

    @Test
    public void should_fail_login_with_invalid_csrf_token() throws Exception {
        mvc.perform(post("/login")
                            .with(csrf().useInvalidToken()))
           .andDo(print())
           .andExpect(status().isForbidden())
           .andExpect(unauthenticated());
    }

    @Test
    public void should_login() throws Exception {
        mvc.perform(formLogin().user(JOHN).password(JOHN_PASSWORD))
           .andDo(print())
           .andExpect(authenticated());
    }

    @Test
    public void should_not_login_with_wrong_password() throws Exception {
        mvc.perform(formLogin().user(JOHN).password("kjhgkgh"))
           .andDo(print())
           .andExpect(unauthenticated());
    }

    @Test
    public void should_respond_with_required_security_headers() throws Exception {
        givenLoggedInUser();

        whenRequestingStartPage();

        assertCommonSecurityHeaders(s -> resultActions.andReturn().getResponse().getHeader(s));
    }

    private void givenLoggedInUser() throws Exception {
        resultActions = mvc.perform(formLogin("/login").user(JOHN).password(JOHN_PASSWORD))
                           .andDo(print())
                           .andExpect(authenticated());
    }

    private void whenRequestingStartPage() throws Exception {
        final MockHttpSession session = (MockHttpSession) resultActions.andReturn().getRequest().getSession();
        resultActions = mvc.perform(get("/").session(session)
                                            .header("X-forwarded-proto", "https")
                                            .header("X-forwarded-for", "127.0.0.1"))
                           .andDo(print())
                           .andExpect(status().isOk());
    }

}
