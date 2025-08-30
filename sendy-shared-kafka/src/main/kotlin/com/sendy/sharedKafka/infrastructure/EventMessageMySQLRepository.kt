package com.sendy.sharedKafka.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.sendy.sharedKafka.domain.EventMessage
import com.sendy.sharedKafka.domain.EventMessageRepository
import com.sendy.sharedKafka.domain.EventStatus
import org.springframework.stereotype.Repository

@Repository
class EventMessageMySQLRepository(
    private val eventMessageJpaRepository: EventMessageJpaRepository,
    private val objectMapper: ObjectMapper,
) : EventMessageRepository {
    override fun <T> saveReady(eventMessage: EventMessage<T>) {
        eventMessageJpaRepository.save(
            EventMessageJpaEntity(
                id = eventMessage.id,
                aggregateId = eventMessage.aggregateId,
                source = eventMessage.source,
                status = EventStatus.READY,
                type = eventMessage.type,
                payload = objectMapper.writeValueAsString(eventMessage.payload),
            ),
        )
    }
}
