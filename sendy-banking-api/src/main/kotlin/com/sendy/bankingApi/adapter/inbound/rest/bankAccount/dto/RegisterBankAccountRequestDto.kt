package com.sendy.bankingApi.adapter.inbound.rest.bankAccount.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RegisterBankAccountRequestDto(
    @field:NotNull
    val userId: Long,
    @field:NotBlank
    val bankName: String,
    @field:NotBlank
    val bankAccountNumber: String,
)
