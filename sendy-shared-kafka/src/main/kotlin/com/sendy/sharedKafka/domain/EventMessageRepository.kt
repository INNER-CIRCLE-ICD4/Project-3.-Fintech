package com.sendy.sharedKafka.domain

interface EventMessageRepository {
    fun <T> saveReady(eventMessage: EventMessage<T>)

    fun savePublish(eventId: Long)

    fun getUserEventMessage(): List<EventMessage<String>>
}
