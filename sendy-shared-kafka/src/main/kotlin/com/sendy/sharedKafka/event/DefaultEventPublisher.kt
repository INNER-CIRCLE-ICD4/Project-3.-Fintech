package com.sendy.sharedKafka.event

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
internal class DefaultEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
) : EventPublisher {
    override fun <T> publish(
        topic: String,
        data: EventMessage<T>,
    ) {
        kafkaTemplate.send(topic, data)
    }

    override fun <T> publish(
        topic: String,
        data: T,
    ) {
        kafkaTemplate.send(topic, data)
    }

    override fun <T> publish(
        topic: String,
        key: String,
        data: EventMessage<T>,
    ) {
        kafkaTemplate.send(topic, key, data)
    }
}
