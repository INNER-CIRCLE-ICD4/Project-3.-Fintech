package com.sendy.sharedKafka.event

interface EventPublisher {
    fun <T> publish(
        topic: String,
        data: EventMessage<T>,
    )

    fun <T> publish(
        topic: String,
        key: String,
        data: EventMessage<T>,
    )
}
