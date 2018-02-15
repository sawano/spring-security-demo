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
