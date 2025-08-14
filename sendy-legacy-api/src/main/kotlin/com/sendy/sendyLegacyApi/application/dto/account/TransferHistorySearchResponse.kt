package com.sendy.sendyLegacyApi.application.dto.account

data class TransferHistorySearchResponse(
    val userId : Long,
    val tx_type : String,
    val amount : Long,
    val balance_after : Long,
    val description: String,
    val created_at: String
)
