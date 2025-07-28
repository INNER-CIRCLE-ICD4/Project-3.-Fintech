package com.sendy.application.usecase.account.command.dto

import com.sendy.domain.enum.TransactionHistoryTypeEnum
import java.time.LocalDateTime

data class TransactionHistoryDto(
    val accountId: Long,
    val type: TransactionHistoryTypeEnum,
    val amount: Long,
    val balanceAfter: Long,
    val description: String? = null,
    val transferId: Long? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

