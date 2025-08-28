package com.sendy.sendyLegacyApi.interfaces.rest.transfer

data class ReservedTransferRequestDto(
    val transferIds: List<ReservedTransferItemRequestDto>,
) {
    data class ReservedTransferItemRequestDto(
        val value: Long,
    )
}
