package org.globwizards.kafkaworker.controller

import mu.KotlinLogging
import org.globwizards.kafkaworker.service.KafkaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/kafka/messages")
class KafkaController(
    private val kafkaService: KafkaService
) {
    private val log = KotlinLogging.logger {}

    @GetMapping
    fun consumeMessage(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(kafkaService.receive())
    }

    @PostMapping
    fun produceMessage(@RequestBody message: String): ResponseEntity<Any> {
        return try {
            log.info("Received message to produce to Kafka: {}...", message.take(5))
            kafkaService.send(message).get()
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            log.error("Exception has occurred when sending message to Kafka: {}", e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message: " + e.message)
        }
    }
}
