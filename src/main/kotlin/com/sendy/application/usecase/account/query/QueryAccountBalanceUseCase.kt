package com.sendy.application.usecase.account.query

import com.sendy.domain.account.AccountEntity

interface QueryAccountBalanceUseCase {
    fun execute(userId: Long, accountNumber: String): AccountEntity
}
