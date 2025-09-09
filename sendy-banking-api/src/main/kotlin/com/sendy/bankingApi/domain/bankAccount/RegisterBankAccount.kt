package com.sendy.bankingApi.domain.bankAccount

import com.sendy.bankingApi.domain.vo.UserId

class RegisterBankAccount(
    val id: String,
    val userId: UserId,
    val bankName: String,
    val bankAccountNumber: String,
    val linkedStatusIsValid: Boolean,
)
