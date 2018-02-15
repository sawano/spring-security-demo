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

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public final class MainController {

    @RequestMapping("/")
    public String index(final ModelMap modelMap) {
        modelMap.addAttribute("host", serverId());
        return "index_template";
    }

    @RequestMapping("/login")
    public String login() {
        return "login_template";
    }

    private static String serverId() {
        try {
            final InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostName() + ":" + localHost.getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
