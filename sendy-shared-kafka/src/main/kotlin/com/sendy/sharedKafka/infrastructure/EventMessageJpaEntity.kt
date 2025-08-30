package com.sendy.sharedKafka.infrastructure

import com.sendy.sharedKafka.domain.EventStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "event_message")
class EventMessageJpaEntity(
    id: Long,
    @Column
    val source: String,
    @Column
    val aggregateId: Long,
    @Column(columnDefinition = "json")
    val payload: String,
    @Enumerated(EnumType.STRING)
    val status: EventStatus,
    @Column
    val type: String,
    @Column
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column
    val publishedAt: LocalDateTime? = null,
) : Identity(id)
