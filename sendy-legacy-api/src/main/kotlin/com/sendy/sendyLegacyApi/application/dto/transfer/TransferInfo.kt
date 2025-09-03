package com.sendy.sendyLegacyApi.application.dto.transfer

data class TransferInfo(
    val deposit: UserId,
    val withdraw: UserId,
) {
    data class UserId(
        val userId: Long,
    )
}
