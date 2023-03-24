FROM debian:bullseye-slim as envsubst_builder

RUN apt-get update && apt-get install -y gettext

FROM confluentinc/cp-kafka-connect-base:latest

RUN confluent-hub install --no-prompt mongodb/kafka-connect-mongodb:1.9.1

ENV CONNECT_PLUGIN_PATH="/usr/share/java,/usr/share/confluent-hub-components"

ENV DB_HOST documentDB
ENV DB_PORT 27017
ENV DB_DATABASE messages

COPY entrypoint.sh /etc/confluent/docker/entrypoint.sh
COPY documentdb-kafka-connect.template.json       /etc/kafka/documentdb-kafka-connect.template.json
COPY documentdb-kafka-connect.template-local.json /etc/kafka/documentdb-kafka-connect.template-local.json

USER root
RUN yum update -y && yum install -y curl openssl perl
COPY import_rds_certs.sh /certs/import_rds_certs.sh
RUN /certs/import_rds_certs.sh trustme

COPY --from=envsubst_builder /usr/bin/envsubst /usr/local/bin/envsubst

ENTRYPOINT ["/etc/confluent/docker/entrypoint.sh"]