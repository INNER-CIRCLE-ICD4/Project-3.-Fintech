package com.sendy.sharedKafka.infrastructure

import org.springframework.data.jpa.repository.JpaRepository

interface EventMessageJpaRepository : JpaRepository<EventMessageJpaEntity, Long>
