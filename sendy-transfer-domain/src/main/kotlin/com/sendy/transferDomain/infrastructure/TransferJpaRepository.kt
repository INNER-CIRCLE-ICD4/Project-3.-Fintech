package com.sendy.transferDomain.infrastructure

import org.springframework.data.jpa.repository.JpaRepository

internal interface TransferJpaRepository : JpaRepository<TransferEntity, Long>
