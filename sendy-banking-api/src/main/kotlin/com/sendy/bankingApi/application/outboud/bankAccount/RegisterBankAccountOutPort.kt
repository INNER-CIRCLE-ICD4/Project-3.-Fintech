package com.sendy.bankingApi.application.outboud.bankAccount

import com.sendy.bankingApi.domain.bankAccount.RegisterBankAccount

interface RegisterBankAccountOutPort {
    fun createRegisterBankAccount(registerBankAccount: RegisterBankAccount): String
}
