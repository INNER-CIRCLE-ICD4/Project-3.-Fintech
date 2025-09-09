package com.sendy.bankingApi.application.outboud.bankAccount

import com.sendy.bankingApi.domain.bankAccount.RequestFirmBanking

interface RequestFirmBankingOutPort {
    fun createRequestFirmBanking(requestFirmBanking: RequestFirmBanking): RequestFirmBanking

    fun updateRequestFirmBanking(requestFirmBanking: RequestFirmBanking): RequestFirmBanking
}
