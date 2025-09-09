package com.sendy.bankingApi.adapter.inbound.rest.bankAccount.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RequestFirmBankingRequestDto(
    /**
     * from -> to 실물 계좌로 요청을 하기 위한 Request
     *
     */
    @field:NotBlank
    val fromBankName: String,
    @field:NotBlank
    val fromBankAccountNumber: String,
    @field:NotBlank
    val toBankName: String,
    @field:NotBlank
    val toBankAccountNumber: String,
    @field:NotNull
    val moneyAccount: Long, // only won
)
