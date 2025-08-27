package com.sendy.sharedKafka.event

interface EventFailed {

    fun <T> publishFailed(
        topic: String,
        data: EventMessage<T>,
    )

    fun <T> publishFailed(
        topic: String,
        key: String,
        data: EventMessage<T>,
    )
}