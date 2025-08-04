package com.sendy.application.usecase.account.command

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.dto.account.CreateAccountResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CreateAccountService(
    private val createAccountUseCase: CreateAccountUseCaseImpl
) : CreateAccountUseCase {
    override fun execute(request: CreateAccountRequest): CreateAccountResponse {
        return createAccountUseCase.execute(request)
    }
}
