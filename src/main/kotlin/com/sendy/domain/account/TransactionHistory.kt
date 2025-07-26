package com.sendy.domain.account

import com.sendy.domain.enum.TransactionHistoryTypeEnum
import java.time.LocalDateTime

data class TransactionHistory(
    val id: Long,
    val type: TransactionHistoryTypeEnum,
    val amount: Long,
    val balanceAfter: Long,
    val description: String? = null,
    val createdAt: LocalDateTime,
    val transferId: Long? = null,
)
