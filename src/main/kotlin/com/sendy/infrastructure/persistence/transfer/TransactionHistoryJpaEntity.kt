package com.sendy.infrastructure.persistence.transfer

import com.sendy.infrastructure.persistence.Identity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "transaction_history")
class TransactionHistoryJpaEntity(
    id: String,
    @Column(name = "tx_type")
    var type: String,
    @Column
    var amount: Long,
    @Column
    var balanceAfter: Long,
    @Column(nullable = true)
    var description: String? = null,
    @Column
    val createdAt: LocalDateTime,
    @Column
    var transferId: String? = null,
) : Identity(id)
