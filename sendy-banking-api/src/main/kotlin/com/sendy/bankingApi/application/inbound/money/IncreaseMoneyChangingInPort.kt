package com.sendy.bankingApi.application.inbound.money

import com.sendy.bankingApi.domain.vo.UserId
import java.time.LocalDateTime

interface IncreaseMoneyChangingInPort {
    fun increaseMoneyChanging(command: IncreaseMoneyChangingCommand): IncreaseMoneyChangingResult

    data class IncreaseMoneyChangingCommand(
        val targetUserId: UserId,
        val amount: Long,
    )

    data class IncreaseMoneyChangingResult(
        val id: String,
        val targetUserId: Long,
        val changingType: Int,
        val changingMoneyAmount: Long,
        val changingMoneyStatus: Int,
        val createdAt: LocalDateTime,
    )
}
