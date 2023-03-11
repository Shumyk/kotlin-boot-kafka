package org.globwizards.kafkaworker.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

const val MESSAGE_COLLECTION = "messages"

@Document(collection = MESSAGE_COLLECTION)
data class Message(
    @Id val id: String = UUID.randomUUID().toString(),
    val body: String,
    val timestamp: Instant = Instant.now()
)
