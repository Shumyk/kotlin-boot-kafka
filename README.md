## Kotlin Boot Mongo-Kafka Project

This repository contains a Kotlin-based Spring Boot application, demonstrating the use of Kafka for message streaming and simple interaction with Mongo-like databases. Below you'll find a brief description of the project, prerequisites, and instructions on how to run the project locally.

### Table of Contents
1. [Description](#description)
2. [Prerequisites](#prerequisites)
3. [Running the Project Locally](#running-the-project-locally)
4. [Testing The Architecture](#testing-the-architecture)

### Description
The Kotlin Boot Mongo-Kafka project is a Spring Boot application developed in Kotlin that showcases the integration of Kafka for message streaming and MongoDB. It consists of a producer and consumer that communicate via Kafka topics and the persistence layer for communicating with MongoDB.

### Prerequisites
To run this project, you'll need the following tools installed on your machine:

* Java Development Kit (JDK) 17 or later
* Gradle
* Docker
* Docker Compose

### Running the Project Locally

#### Info 
As this Kotlin app uses Kafka and Mongo (DocumentDB) - we need to bring up also other services.

- Zookeeper *[confluentinc/cp-zookeeper]*
- Kafka *[confluentinc/cp-kafka]*
- Mongo (as replica set) *[davybello/mongo-replica-set]*
- Kotlin Boot Mongo-Kafka App *[shumyk/mongo-kafka-connect]*
- Mongo-Kafka Connector *[shumyk/mongo-kafka-connect]*

Two last on the list is our services, and we need to build them beforehand. 

#### Build - Kotlin app...
...which is responsible for listening on the *8080* port and processing HTTP request.\
There are two endpoints for Kafka:
- ***GET***   localhost:8080/kafka/messages - returns all messages from **items** topic
- ***POST***  localhost:8080/kafka/messages - publishes a message to **items** topic

Also, similar endpoints for MongoDB:
- ***GET*** localhost:8080/documentdb/messages - fetches all records from **messages** collection
- ***POST*** localhost:8080/documentdb/messages - inserts a record into **messages** collection

Initially, we need to build a Docker image of our app.
```shell
docker build --tag=shumyk/kotlin-km-service --force-rm .
```
*shumyk/kotlin-km-service* is expected by *docker-compose.yaml*, and if you are changing it - create a Docker image with the proper tag!\
At this point, the Kotlin app is ready to run.

#### Build - Mongo-Kafka Connector...
...which is responsible for listening to new records in MongoDB's collection **messages** and producing it to Kafka topic **items**.

Let's build it Docker image: 
```shell
docker build --tag=shumyk/mongo-kafka-connect --force-rm documentdb-kafka-connect 
```
*shumyk/mongo-kafka-connect* is expected by *docker-compose.yaml*, and if you are changing it - create a Docker image with the proper tag!

#### Running

To run everything together locally, we will use Docker and Compose tools. Ensure you have Docker Desktop running on your machine, and let's continue.\
First, execute run commands for Zookeeper, Kafka, and Mongo:
```shell
docker-compose up zookeeper kafka documentDB
```
Note: you can specify **-d** to run the command in detached mode, but you won't see logs. I recommend running without **-d** flag. Hence you will see services logs. Following services you run in another window.\
Also, you can use this command to track the status of your containers:
```shell
watch "docker ps"
```
Wait up to one minute for services to start, and then proceed with the Kotlin app:
```shell
docker-compose up kafka-service
```
You should not see any exceptions in Kotlin's logs. After the application starts, you can call its endpoints to verify that Kafka and Mongo communication works as expected.\
Finally, let's run Kafka Connector:
```shell
docker-compose up documentdb-kafka-connect
```
Follow logs of the Connector to verify that there are no errors and custom script registers the Mongo connector successfully.\
In logs, search for 'Creating MongoDB connector...' or 'Connector payload to create:' and logs after this.
To ensure that script successfully registers the Connector, call **localhost:8083/connectors** - it should return *["document-source-connector"]*.

### Testing The Architecture
Now all services are up and running, let's verify their functionality! 
In the new terminal window, create a console consumer for Kafka's topic **items**:
```shell
docker exec -it <insert Kafka container ID> kafka-console-consumer --bootstrap-server localhost:9092 --from-beginning --topic items
```
Kafka container ID you can get from **docker ps**

Now we have an eye on the **items** topic, let's produce some messages to Mongo's collection **messages**. You can run it from the terminal or Postman:
```shell
curl -L 'localhost:8080/documentdb/messages' \
-H 'Content-Type: text/plain' \
-d 'putin Huilo!'
```
After a short moment, this message should appear in the Kafka console consumer you opened earlier.

If so, congratulations! If no, please check the errors and refer to the code and the internet for answers.
