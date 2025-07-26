package com.sendy.application.dto.account

data class CreateAccountRequest(
    val accountNumber: String,
    val userId: Long,
    val isPrimary: Boolean,
    val isLimitedAccount: Boolean,
)

data class CreateAccountResponse(
    val accountNumber: String
)

