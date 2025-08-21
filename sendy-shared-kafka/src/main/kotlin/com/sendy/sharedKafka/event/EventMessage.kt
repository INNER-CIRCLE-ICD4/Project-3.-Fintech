package com.sendy.sharedKafka.event

import java.time.LocalDateTime

data class EventMessage<T>(
    val eventId: Long,
    val aggregateId: Long,
    val payload: T,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
