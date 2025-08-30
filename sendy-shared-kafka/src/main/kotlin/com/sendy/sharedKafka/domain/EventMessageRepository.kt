package com.sendy.sharedKafka.domain

interface EventMessageRepository {
    fun <T> saveReady(eventMessage: EventMessage<T>)
}
