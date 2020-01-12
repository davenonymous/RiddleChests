#!/bin/bash

ALIAS=davenonymous/riddlechests

KEYTOOL_BIN=keytool

rm -f ./keystore.jks
${KEYTOOL_BIN} -genkey -alias ${ALIAS} \
	-keyalg RSA -keysize 2048 -keystore ./keystore.jks \
	-storepass ${JARSIGN_KEY_PASS} -keypass ${JARSIGN_KEY_PASS} \
	-noprompt -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown"

echo "Created key store file"

hash=$(${KEYTOOL_BIN} -list -alias davenonymous/riddlechests -keystore ./keystore.jks -storepass ${JARSIGN_KEY_PASS} | grep SHA1 | awk '{print $4}' | sed 's/://g' | tr '[:upper:]' '[:lower:]')

echo "" >> gradle.properties
echo keyStore=./keystore.jks >> gradle.properties
echo keyStoreAlias=${ALIAS} >> gradle.properties
echo signSHA1=${hash} >> gradle.properties

echo "Created signing hash: ${hash}"
