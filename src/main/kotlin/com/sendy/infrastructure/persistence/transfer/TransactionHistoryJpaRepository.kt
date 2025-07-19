package com.sendy.infrastructure.persistence.transfer

import org.springframework.data.jpa.repository.JpaRepository

interface TransactionHistoryJpaRepository :
    JpaRepository<
        TransactionHistoryJpaEntity,
        Long,
    >
