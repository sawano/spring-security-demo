#!/bin/bash -e

pushd "${BASH_SOURCE%/*}/.."

pushd "src/main/resources/ssl"

rm -f server.crt
rm -f server.csr
rm -f myPrivate.key
rm -f my-keystore.jks
rm -f my-keystore.p12
rm -f truststore.jks

#
#openssl genrsa -des3 -passout pass:x -out server.pass.key 2048
#openssl rsa -passin pass:x -in server.pass.key -out server.key
#rm server.pass.key
#openssl req -new -key server.key -out server.csr
#openssl x509 -req -sha256 -days 365 -in server.csr -signkey server.key -out server.crt
#
#keytool -genkeypair -alias spring-security-demo -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore my-keystore.p12 -validity 3650
#keytool -list -v -storetype pkcs12 -keystore my-keystore.p12
#keytool -import -alias spring-security-demo -file server.crt -keystore my-keystore.p12 -storepass spring-security-demo


#Use password spring-security-demo everywhere!
#openssl genrsa -des3 -out myPrivate.key 1024
#openssl req -new -key myPrivate.key -out server.csr # Make sure to type localhost as common name!
#openssl x509 -req -days 3650 -in server.csr -signkey myPrivate.key -out server.crt
#
#keytool -import -file server.crt -alias spring-security-demo -keystore truststore.jks
#openssl pkcs12 -export -in server.crt -inkey myPrivate.key -certfile server.crt -name "examplecert" -out my-keystore.p12
#keytool -importkeystore -srckeystore my-keystore.p12 -srcstoretype pkcs12 -destkeystore my-keystore.jks -deststoretype JKS


keytool -genkey -alias spring-security-demo -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore my-keystore.p12 -validity 730
