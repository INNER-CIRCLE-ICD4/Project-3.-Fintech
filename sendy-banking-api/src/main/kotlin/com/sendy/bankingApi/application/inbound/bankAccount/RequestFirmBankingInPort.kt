package com.sendy.bankingApi.application.inbound.bankAccount

import com.sendy.bankingApi.domain.vo.FirmBankingId

interface RequestFirmBankingInPort {
    fun requestFirmBanking(command: RequestFirmBankingCommand): FirmBankingId

    data class RequestFirmBankingCommand(
        val fromBankName: String,
        val fromBankAccountNumber: String,
        val toBankName: String,
        val toBankAccountNumber: String,
        val moneyAccount: Long, // only won
    )
}
