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

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public final class CommonSecurityAssertions {

    public static void assertCommonSecurityHeaders(final Function<String, String> headerProvider) {
        new CommonSecurityAssertions(headerProvider).verify();
    }

    private final Function<String, String> headerProvider;

    private void verify() {
        thenHeader("X-Content-Type-Options").hasValue("nosniff");
        thenHeader("X-XSS-Protection").hasValue("1; mode=block");
        thenHeader("X-Frame-Options").hasValue("DENY");
        thenHeader("Cache-Control").hasValue("no-cache, no-store, max-age=0, must-revalidate");
        thenHeader("Pragma").hasValue("no-cache");
        thenHeader("Expires").hasValue("0");
        thenHeader("Referrer-Policy").hasValue("same-origin");
        thenHeader("Content-Security-Policy").hasValue(SecurityConfiguration.EXAMPLE_CSP);
    }

    private CommonSecurityAssertions(final Function<String, String> headerProvider) {
        this.headerProvider = headerProvider;
    }

    private ExpectedHeader thenHeader(final String headerName) {
        return new ExpectedHeader(headerName);
    }

    final class ExpectedHeader {
        final String headerName;

        public ExpectedHeader(final String headerName) {
            this.headerName = headerName;
        }

        public void hasValue(final String value) {
            final String actual = headerProvider.apply(headerName);
            assertEquals("Expected header '" + headerName + "' to have value: '" + value + "', but was: '" + actual + "'.",
                         value,
                         actual);
        }
    }
}
