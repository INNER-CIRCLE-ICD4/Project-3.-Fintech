package com.sendy.bankingApi.application.inbound.bankAccount

import com.sendy.bankingApi.domain.vo.BankAccountId
import com.sendy.bankingApi.domain.vo.UserId

interface RegisterBankAccountInPort {
    fun registerBankAccount(command: RegisterBankAccountCommand): BankAccountId?

    data class RegisterBankAccountCommand(
        val bankName: String,
        val bankAccountNumber: String,
        val userId: UserId,
    )
}
