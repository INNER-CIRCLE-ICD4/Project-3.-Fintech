package com.sendy.bankingApi.domain.bankAccount

data class RequestFirmBanking(
    val id: String,
    val fromBankName: String,
    val fromBankAccountNumber: String,
    val toBankName: String,
    val toBankAccountNumber: String,
    val moneyAmount: Long,
    val firmBankingStatus: Int, // 0: 요청, 1: 완료, 2: 실패
)
