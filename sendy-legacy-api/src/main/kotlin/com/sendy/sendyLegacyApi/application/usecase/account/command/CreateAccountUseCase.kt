package com.sendy.sendyLegacyApi.application.usecase.account.command

import com.sendy.sendyLegacyApi.application.dto.account.CreateAccountRequest
import com.sendy.sendyLegacyApi.application.dto.account.CreateAccountResponse

interface CreateAccountUseCase {
    fun execute(request: CreateAccountRequest): CreateAccountResponse
}
