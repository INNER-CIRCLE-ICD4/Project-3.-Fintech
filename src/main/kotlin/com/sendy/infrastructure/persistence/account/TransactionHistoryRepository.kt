package com.sendy.infrastructure.persistence.account

import com.sendy.domain.account.TransactionHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionHistoryRepository : JpaRepository<TransactionHistoryEntity, Long>
