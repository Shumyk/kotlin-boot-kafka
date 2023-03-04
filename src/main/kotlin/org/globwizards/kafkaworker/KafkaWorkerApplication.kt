package org.globwizards.kafkaworker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@EnableKafka
@SpringBootApplication
class KafkaWorkerApplication

fun main(args: Array<String>) {
	runApplication<KafkaWorkerApplication>(*args)
}
