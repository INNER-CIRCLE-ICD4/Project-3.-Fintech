package com.sendy.sharedKafka.domain

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
