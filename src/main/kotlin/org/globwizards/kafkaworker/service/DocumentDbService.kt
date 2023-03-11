package org.globwizards.kafkaworker.service

import org.globwizards.kafkaworker.data.MESSAGE_COLLECTION
import org.globwizards.kafkaworker.data.Message
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service

@Service
class DocumentDbService(
    private val mongoTemplate: MongoTemplate
) {

    fun save(msg: Message) {
        mongoTemplate.save(msg, MESSAGE_COLLECTION)
    }

    fun messages(): List<Message> {
        return mongoTemplate.findAll(Message::class.java, MESSAGE_COLLECTION)
    }
}