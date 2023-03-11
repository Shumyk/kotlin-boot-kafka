package org.globwizards.kafkaworker.config

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.TopicPartition
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory

private const val PARTITION = 1
private const val REPLICAS = 1

@Configuration
class KafkaConsumerConfig(
    @Value("\${kafka.topics}") val topic: String,
) {

    @Bean
    fun topic(): NewTopic {
        return TopicBuilder.name(topic)
            .partitions(PARTITION)
            .replicas(REPLICAS)
            .build()
    }

    @Bean
    fun itemsConsumer(consumerFactory: ConsumerFactory<String, String>): Consumer<String, String> {
        val itemsConsumer = consumerFactory.createConsumer()
        itemsConsumer.assign(listOf(TopicPartition(topic, 0)))
        return itemsConsumer
    }
}
