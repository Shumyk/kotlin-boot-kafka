FROM openjdk:17-alpine

COPY build/libs/kafka-worker*.jar KafkaService.jar

ENTRYPOINT ["java", "-jar", "KafkaService.jar"]
