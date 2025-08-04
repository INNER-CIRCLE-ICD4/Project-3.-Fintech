package com.sendy.application.usecase.account.command

import com.sendy.domain.account.AccountEntity

interface SaveAccountUseCase {
    fun execute(accountEntity: AccountEntity): AccountEntity
}