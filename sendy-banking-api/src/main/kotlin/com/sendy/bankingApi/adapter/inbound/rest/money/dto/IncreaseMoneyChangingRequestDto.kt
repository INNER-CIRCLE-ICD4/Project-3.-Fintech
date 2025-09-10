package com.sendy.bankingApi.adapter.inbound.rest.money.dto

import jakarta.validation.constraints.NotNull

data class IncreaseMoneyChangingRequestDto(
    @field:NotNull
    val targetUserId: Long,
    @field:NotNull
    val amount: Long,
)
