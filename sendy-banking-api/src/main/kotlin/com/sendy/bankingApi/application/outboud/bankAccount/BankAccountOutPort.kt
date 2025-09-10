package com.sendy.bankingApi.application.outboud.bankAccount

import com.sendy.bankingApi.domain.bankAccount.RegisterBankAccount
import com.sendy.bankingApi.domain.vo.UserId

interface BankAccountOutPort {
    fun getBankAccountByUserId(userId: UserId): RegisterBankAccount
}
