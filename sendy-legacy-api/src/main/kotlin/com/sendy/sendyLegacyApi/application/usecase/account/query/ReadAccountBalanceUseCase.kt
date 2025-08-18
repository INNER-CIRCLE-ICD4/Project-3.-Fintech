package com.sendy.sendyLegacyApi.application.usecase.account.query

import com.sendy.sendyLegacyApi.application.dto.account.AccountBalanceResponse

interface ReadAccountBalanceUseCase {
    fun execute(
        userId: Long,
        accountNumber: String,
    ): AccountBalanceResponse
}
