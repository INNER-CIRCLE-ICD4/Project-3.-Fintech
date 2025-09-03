package com.sendy.sharedKafka.domain

interface EventClose {
    fun <T> publishClose(
        topic: String,
        data: EventMessage<T>,
    )

    fun <T> publishClose(
        topic: String,
        key: String,
        data: EventMessage<T>,
    )
}
