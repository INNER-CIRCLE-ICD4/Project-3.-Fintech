package com.sendy.transferDomain.domain

import com.sendy.transferDomain.domain.enum.TransferStatusEnum
import java.time.LocalDateTime

class Transfer(
    val id: Long,
    val sendUserId: Long,
    val sendAccountNumber: String,
    val receivePhoneNumber: String?,
    val receiveAccountNumber: String?,
    var amount: Long,
    var status: TransferStatusEnum,
    val scheduledAt: LocalDateTime?,
    val requestedAt: LocalDateTime,
    var completedAt: LocalDateTime?,
    val reason: String?,
)
