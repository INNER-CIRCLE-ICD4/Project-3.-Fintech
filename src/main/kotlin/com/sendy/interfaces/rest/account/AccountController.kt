package com.sendy.interfaces.rest.account

import com.sendy.application.dto.account.AccountBalanceResponse
import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.dto.account.CreateAccountResponse
import com.sendy.application.usecase.account.command.CreateAccountUseCase
import com.sendy.application.usecase.account.query.QueryAccountBalanceUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("accounts")
class AccountController(
    private val createAccountUseCase: CreateAccountUseCase,
    private val queryAccountBalanceUseCase: QueryAccountBalanceUseCase,
) {
    @PostMapping
    fun createAccount(
        @RequestBody request: CreateAccountRequest,
    ): CreateAccountResponse = createAccountUseCase.execute(request)

    @GetMapping("/balance")
    fun getAccountBalance(
        @RequestParam userId: Long,
        @RequestParam accountNumber: String
    ): AccountBalanceResponse {
        val account = queryAccountBalanceUseCase.execute(userId, accountNumber)
        return AccountBalanceResponse(account.accountNumber, account.balance)
    }
}
