package org.globwizards.kafkaworker.service

import org.apache.kafka.clients.consumer.Consumer
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaOperations
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaService(
    @Value("\${kafka.topics}") val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val itemsConsumer: Consumer<String, String>
) {

    fun send(message: String) {
        kafkaTemplate.send(topic, message)
    }

    fun receive(): List<String> {
        itemsConsumer.seekToBeginning(itemsConsumer.assignment())
        val records = itemsConsumer.poll(KafkaOperations.DEFAULT_POLL_TIMEOUT)
        val values = records.map { record -> record.value() }
        if (values.isEmpty())
            return listOf("No message available.")
        return values
    }
}
