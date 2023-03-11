package org.globwizards.kafkaworker.controller

import mu.KotlinLogging
import org.globwizards.kafkaworker.data.Message
import org.globwizards.kafkaworker.service.DocumentDbService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/documentdb/messages")
class DocumentDBController(
    private val documentDbService: DocumentDbService
) {
    private val log = KotlinLogging.logger {}

    @GetMapping
    fun fetchMessages(): ResponseEntity<List<Message>> {
        return ResponseEntity.ok(documentDbService.messages())
    }

    @PostMapping
    fun saveMessage(@RequestBody message: String): ResponseEntity<Any> {
        return try {
            log.info("Received message to save to DocumentDB: {}...", message.take(5))
            documentDbService.save(Message(body = message))
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            log.error("Exception has occurred during saving message to DocumentDB: {}", e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save message: " + e.message)
        }
    }
}