package com.sendy.application.dto.account

data class CreateAccountRequest(
    val userId: Long,
    val isPrimary: Boolean,
    val isLimitedAccount: Boolean,
    val password: String,
)

data class CreateAccountResponse(
    val accountNumber: String
)

