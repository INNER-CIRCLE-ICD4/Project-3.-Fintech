package com.sendy.consumer.handler

interface EventHandlerConsumer<T> {
    fun handle(event: T)
    fun supports(eventType: String): Boolean
}