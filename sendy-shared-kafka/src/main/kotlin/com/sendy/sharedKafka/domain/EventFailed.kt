package com.sendy.sharedKafka.domain

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
