package com.sendy.application.usecase.account

import com.sendy.application.usecase.account.command.CreateAccountNumberUseCase
import com.sendy.application.usecase.account.command.GeneratedAccountNumberUseCase
import com.sendy.support.util.AccountNumberValidator
import org.springframework.stereotype.Service

@Service
class GeneratedAccountNumberService (
    private val createAccountNumberUseCase: CreateAccountNumberUseCase
): GeneratedAccountNumberUseCase {
    override fun execute(): String {
        val generatedAccountNumber = createAccountNumberUseCase.generate()
        AccountNumberValidator.validateFormat(generatedAccountNumber)
        return generatedAccountNumber
    }
}