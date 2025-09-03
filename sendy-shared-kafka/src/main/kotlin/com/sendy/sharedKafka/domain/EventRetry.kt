package com.sendy.sharedKafka.domain

interface EventRetry {
    fun <T> publishRetry(
        topic: String,
        data: EventMessage<T>,
    )

    fun <T> publishRetry(
        topic: String,
        key: String,
        data: EventMessage<T>,
    )
}
