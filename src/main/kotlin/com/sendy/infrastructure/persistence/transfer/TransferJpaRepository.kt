package com.sendy.infrastructure.persistence.transfer

import org.springframework.data.jpa.repository.JpaRepository

interface TransferJpaRepository : JpaRepository<TransferJpaEntity, Long>
