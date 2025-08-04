package com.sendy.application.usecase.account.command

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.domain.account.AccountEntity

interface CreateAccountEntityUseCase {
    fun execute(request: CreateAccountRequest, accountNumber: String, encryptedPassword: String): AccountEntity
}