package com.sendy.bankingApi.application.outboud.bankAccount

interface RetrieveBankAccountInfoOutPort {
    fun retrieveBankAccountInfo(request: RetrieveBankAccountRequestDto): BankAccountInfoResponseDto

    data class RetrieveBankAccountRequestDto(
        val bankName: String,
        val bankAccountNumber: String,
    )

    data class BankAccountInfoResponseDto(
        val bankName: String,
        val bankAccountNumber: String,
        val isValid: Boolean,
    )
}
