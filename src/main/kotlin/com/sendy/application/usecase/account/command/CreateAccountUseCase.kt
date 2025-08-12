package com.sendy.application.usecase.account.command

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.dto.account.CreateAccountResponse

interface CreateAccountUseCase {
    fun execute(request: CreateAccountRequest): CreateAccountResponse
}

