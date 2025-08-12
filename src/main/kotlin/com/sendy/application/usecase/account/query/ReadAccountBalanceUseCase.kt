package com.sendy.application.usecase.account.query

import com.sendy.application.dto.account.AccountBalanceResponse

interface ReadAccountBalanceUseCase {
    fun execute(
        userId: Long,
        accountNumber: String,
    ): AccountBalanceResponse
}
