FROM openjdk:17-alpine

RUN apk update \
    && apk upgrade \
    && apk --no-cache add curl openssl perl

COPY import_rds_certs.sh /certs/import_rds_certs.sh
ARG trustStorePassword
RUN /certs/import_rds_certs.sh ${trustStorePassword}

ENV TRUST_STORE_PASSWORD=${trustStorePassword}

COPY build/libs/kafka-worker*.jar KafkaService.jar

CMD java -jar \
    -Djavax.net.ssl.trustStore=/certs/rds-truststore.jks \
    -Djavax.net.ssl.trustStorePassword=${TRUST_STORE_PASSWORD} \
    KafkaService.jar
