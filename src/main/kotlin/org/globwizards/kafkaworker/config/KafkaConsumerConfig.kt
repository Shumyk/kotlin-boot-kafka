package org.globwizards.kafkaworker.config

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.TopicPartition
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaConsumerConfig(
    @Value("\${kafka.topics}") val topic: String,
) {

    @Bean
    fun itemsConsumer(
        consumerFactory: ConsumerFactory<String, String>,
        kafkaAdmin: KafkaAdmin
    ): Consumer<String, String> {
        val itemsConsumer = consumerFactory.createConsumer()
        itemsConsumer.assign(topicPartitions(kafkaAdmin, topic))
        return itemsConsumer
    }

    private fun topicPartitions(kafkaAdmin: KafkaAdmin, topic: String): List<TopicPartition>? {
        return kafkaAdmin.describeTopics(topic)[topic]
            ?.partitions()
            ?.map { info -> TopicPartition(topic, info.partition()) }
    }
}
