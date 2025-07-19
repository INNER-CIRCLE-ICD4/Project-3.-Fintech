package com.sendy.domain.transfer

import com.sendy.domain.enum.TransferStatusEnum
import java.time.LocalDateTime

data class Transfer(
    val id: Long,
    var amount: Long,
    var status: TransferStatusEnum,
    val scheduledAt: LocalDateTime? = null,
    val requestedAt: LocalDateTime,
    var completedAt: LocalDateTime? = null,
    val reason: String? = null,
) {
    fun changeSuccess() {
        status = TransferStatusEnum.SUCCESS
    }
}
