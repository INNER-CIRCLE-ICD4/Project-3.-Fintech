package com.sendy.bankingApi.application.outboud.bankAccount

interface RequestExternalFirmBankingOutPort {
    fun requestExternalFirmBanking(request: RequestExternalRequestDto): RequestExternalResponseDto

    data class RequestExternalRequestDto(
        val fromBankName: String,
        val fromBankAccountNumber: String,
        val toBankName: String,
        val toBankAccountNumber: String,
        val moneyAmount: Long,
    )

    data class RequestExternalResponseDto(
        val resultCode: Int, // 0: 성공, 1: 실패
    )
}
