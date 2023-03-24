#!/bin/bash

compose_connector_payload() {
  if [[ "${PROFILE_ENV}" == "local" ]]; then
    envsubst < /etc/kafka/documentdb-kafka-connect.template-local.json > /etc/kafka/documentdb-kafka-connect.json
  else
    envsubst < /etc/kafka/documentdb-kafka-connect.template.json > /etc/kafka/documentdb-kafka-connect.json
  fi
}

wait_connect() {
  echo "Waiting for Kafka Connect REST API to be ready..."
  while [ $(curl -s -o /dev/null -w %{http_code} localhost:8083/connectors) -eq 000 ]
  do
    echo -e $(date) "Kafka Connect listener state: " $(curl -s -o /dev/null -w %{http_code} localhost:8083/connectors) " (waiting for 200)"
    sleep 5
  done
  echo "Kafka Connect REST API is ready."
}

create_mongo_connector() {
  echo "Creating MongoDB connector..."
  echo "Connector payload to create:"
  cat /etc/kafka/documentdb-kafka-connect.json
  curl -X POST -H "Content-Type: application/json" --data "@/etc/kafka/documentdb-kafka-connect.json" http://localhost:8083/connectors
}

/etc/confluent/docker/run &

compose_connector_payload

wait_connect
create_mongo_connector

sleep infinity