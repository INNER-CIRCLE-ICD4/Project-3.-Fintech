package com.sendy.sendyLegacyApi.application.dto.account

data class CreateAccountRequest(
    val userId: Long,
    val password: String,
    val initBalance: Long,
)

data class CreateAccountResponse(
    val accountNumber: String,
)
