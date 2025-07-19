package com.sendy.infrastructure.persistence.transfer

import com.sendy.domain.enum.TransactionHistoryTypeEnum
import com.sendy.infrastructure.persistence.Identity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "transaction_history")
class TransactionHistoryJpaEntity(
    id: Long,
    @Enumerated(EnumType.STRING)
    @Column(name = "tx_type")
    var type: TransactionHistoryTypeEnum,
    @Column
    var amount: Long,
    @Column
    var balanceAfter: Long,
    @Column(nullable = true)
    var description: String? = null,
    @Column
    val createdAt: LocalDateTime,
    @Column
    var transferId: Long? = null,
) : Identity(id)
