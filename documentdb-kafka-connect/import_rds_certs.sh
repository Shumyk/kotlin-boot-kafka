#!/bin/bash
# first argument is the trustStorePassword

DIR=/certs
TRUSTSTORE=${DIR}/rds-truststore.jks
# password from argument
STORE_PASSWORD=$1

curl -sS "https://s3.amazonaws.com/rds-downloads/rds-combined-ca-bundle.pem" > ${DIR}/rds-combined-ca-bundle.pem
awk 'split_after == 1 {n++;split_after=0} /-----END CERTIFICATE-----/ {split_after=1}{print > "rds-ca-" n ".pem"}' < ${DIR}/rds-combined-ca-bundle.pem

for CERT in rds-ca-*; do
  ALIAS=$(openssl x509 -noout -text -in $CERT | perl -ne 'next unless /Subject:/; s/.*(CN=|CN = )//; print')
  echo "Importing $ALIAS"
  keytool -import -file ${CERT} -alias "${ALIAS}" -storepass ${STORE_PASSWORD} -keystore ${TRUSTSTORE} -noprompt
  rm ${CERT}
done

rm ${DIR}/rds-combined-ca-bundle.pem