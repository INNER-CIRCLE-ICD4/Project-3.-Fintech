package com.sendy.interfaces.rest.account

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.dto.account.CreateAccountResponse
import com.sendy.application.usecase.account.command.CreateAccountUseCase
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val createAccountUseCase: CreateAccountUseCase
) {
    @PostMapping
    fun createAccount(@RequestBody request: CreateAccountRequest): CreateAccountResponse {
        return createAccountUseCase.execute(request)
    }
}

