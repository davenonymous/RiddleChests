#!/bin/bash

ALIAS=davenonymous/riddlechests

KEYTOOL_BIN=keytool
KEYTOOL_BIN=/f/dev/java/bin/keytool.exe

rm -f ./keystore.pfx
${KEYTOOL_BIN} -genkey -alias ${ALIAS} \
	-keyalg RSA -keysize 2048 -keystore ./keystore.pfx -storetype PKCS12 \
	-storepass ${JARSIGN_KEY_PASS} -keypass ${JARSIGN_KEY_PASS} \
	-noprompt -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown"

echo "Created key store file"

hash=$(${KEYTOOL_BIN} -list -alias davenonymous/riddlechests -keystore ./keystore.pfx -storepass ${JARSIGN_KEY_PASS} | grep SHA1 | awk '{print $4}' | sed 's/://g' | tr '[:upper:]' '[:lower:]')

echo "" >> gradle.properties
echo keyStore=./keystore.pfx >> gradle.properties
echo keyStoreAlias=${ALIAS} >> gradle.properties
echo signSHA1=${hash} >> gradle.properties

echo "Created signing hash: ${hash}"
