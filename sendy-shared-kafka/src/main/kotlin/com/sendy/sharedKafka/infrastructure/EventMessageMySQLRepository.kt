package com.sendy.sharedKafka.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.sendy.sharedKafka.domain.EventMessage
import com.sendy.sharedKafka.domain.EventMessageRepository
import com.sendy.sharedKafka.domain.EventStatus
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class EventMessageMySQLRepository(
    private val eventMessageJpaRepository: EventMessageJpaRepository,
    private val objectMapper: ObjectMapper,
    private val jdbcTemplate: JdbcTemplate,
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

    override fun savePublish(eventId: Long) {
        eventMessageJpaRepository.findByIdOrNull(eventId)?.let {
            it.publishedAt = LocalDateTime.now()
            it.status = EventStatus.PUBLISH

            eventMessageJpaRepository.save(it)
        }
    }

    override fun getUserEventMessage(): List<EventMessage<String>> {
        val jdbcClient = JdbcClient.create(jdbcTemplate)

        val sql =
            """
            select em.*
            from event_message em
            where 1=1
            and em.`type` like 'USER%'
            and em.status = 'READY'
            and em.published_at is null
            order by em.created_at desc
            """.trimIndent()

        val list =
            jdbcClient
                .sql(sql)
                .query { rs, _ ->
                    EventMessage(
                        id = rs.getLong("id"),
                        aggregateId = rs.getLong("aggregate_id"),
                        payload = rs.getString("payload"),
                        type = rs.getString("type"),
                        status = EventStatus.entries.find { it.name == rs.getString("status") } ?: EventStatus.READY,
                        source = rs.getString("source"),
                    )
                }.list()

        return list.toList()
    }
}
