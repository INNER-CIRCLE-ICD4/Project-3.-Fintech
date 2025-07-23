package com.sendy.application.usecase.account.command

import com.sendy.domain.account.Account
import com.sendy.domain.account.AccountRepository
import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.dto.account.CreateAccountResponse
import org.springframework.stereotype.Service

@Service
class CreateAccountUseCase(
    private val accountRepository: AccountRepository
) {
    fun execute(request: CreateAccountRequest): CreateAccountResponse {
        return accountRepository.save(
            Account.create(
                accountNumber = request.accountNumber,
                userId = request.userId,
                isPrimary = request.isPrimary,
                isLimitedAccount = request.isLimitedAccount,
            )
        ).let {
            CreateAccountResponse(it.accountNumber)
        }
    }
}

