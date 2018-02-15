# spring-security-demo

Project for showcasing security features in Spring Security and Spring Boot. 


## Security headers

The default setup provides a good selection of HTTP headers. And with a few minor additions the example app is
configured to send the headers shown below. See `SecurityConfiguration` for details.

````
X-Content-Type-Options -> [nosniff]
X-XSS-Protection -> [1; mode=block]
Cache-Control -> [no-cache, no-store, max-age=0, must-revalidate]
Pragma -> [no-cache]
Expires -> [0]
Strict-Transport-Security -> [max-age=31536000 ; includeSubDomains]
X-Frame-Options -> [DENY]
Public-Key-Pins-Report-Only -> [max-age=5184000 ; pin-sha256="d6qzRu9zOECb90Uez27xWltNsj0e1Md7GkYYkVoZWmM=" ; pin-sha256="E9CZ9INDbd+2eRQozYqqbQ2yXLVKB9+xcprMF+44U1g=" ; report-uri="http://example.com/pkp-report" ; includeSubDomains]
Content-Security-Policy -> [script-src 'self' https://safescripts.example.com; object-src https://safeplugins.example.com; report-uri /csp-report-endpoint/]
Referrer-Policy -> [same-origin]
Location -> [https://localhost/login]
Content-Length -> [0]
```` 


## yml config

Some additional security configuration is done in the application.yml:

````yml
server:
  port: 8080
  tomcat:
    remote-ip-header: x-forwarded-for
    protocol-header: x-forwarded-proto
  use-forward-headers: true # render links correctly behind a proxy

security:
  require-ssl: true # we only accept https on this app

````

## Thymeleaf

The example app is using Thymeleaf to render pages. Together with Spring Security and Spring Boot we get automatic CRSF
handling and proper output/input encoding/decoding.

E.g. the Thymeleaf template looks like this:
````html
<form action="#" th:action="@{/login}" method="post">
    <div class="form-group">
        <label for="username">Username</label>:
        <input type="text" id="username" name="username" autofocus="autofocus"/> <br/>
    </div>
    <div class="form-group">
        <label for="password">Password</label>:
        <input type="password" id="password" name="password"/> <br/>
    </div>
    <button type="submit" class="btn btn-primary">Login</button>
</form>
````

And the rendered HTML will have the CSRF token added to it automatically:
````html
<form method="post" enctype="application/x-www-form-urlencoded" action="/login">
    <div class="form-group">
        <label for="username">Username</label>:
        <input type="text" id="username" name="username" autofocus="autofocus" /> <br />
    </div>
    <div class="form-group">
        <label for="password">Password</label>:
        <input type="password" id="password" name="password" /> <br />
    </div>
    <button type="submit" class="btn btn-primary">Login</button>
    <input type="hidden" name="_csrf" value="dc1f7dfd-6c9c-4a8b-9d72-81e1a51de46e" />
</form>
````

 
