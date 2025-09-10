package com.sendy.bankingApi.application.outboud.bankAccount

interface RequestExternalBankAccountAmountInfoOutPort {
    fun requestExternalBankAccountAmountInfo(
        request: RequestExternalBankAccountAmountInfoRequestDto,
    ): RequestExternalBankAccountAmountInfoResponseDto

    data class RequestExternalBankAccountAmountInfoRequestDto(
        val bankName: String,
        val bankAccountNumber: String,
    )

    data class RequestExternalBankAccountAmountInfoResponseDto(
        val amount: Long,
    )
}
