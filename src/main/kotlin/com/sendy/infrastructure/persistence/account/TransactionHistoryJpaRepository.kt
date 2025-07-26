package com.sendy.infrastructure.persistence.account

import org.springframework.data.jpa.repository.JpaRepository

interface TransactionHistoryJpaRepository :
    JpaRepository<
        TransactionHistoryJpaEntity,
        Long,
    >
