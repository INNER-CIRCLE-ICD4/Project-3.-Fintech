package com.sendy.application.dto.transfer

import java.time.LocalDateTime

data class TransferMoneyCommand(
    val userId: Long,
    val senderAccountNumber: String,
    val receiveAccountNumber: String,
    val amount: Long,
    val requestedAt: LocalDateTime,
)
