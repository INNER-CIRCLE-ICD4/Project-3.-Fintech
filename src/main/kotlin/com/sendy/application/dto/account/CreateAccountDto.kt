package com.sendy.application.dto.account

data class CreateAccountRequest(
    //val accountNumber: String, 서버에서 생상하기 때문에 더이상 request로 받지 않아도 됩니다.
    val userId: Long,
    val isPrimary: Boolean,
    val isLimitedAccount: Boolean,
    val password: Int,
)

data class CreateAccountResponse(
    val accountNumber: String
)

