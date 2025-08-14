package com.sendy.sendyLegacyApi.domain.transfer.event

class TransferSucceeded(
    val transferId: Long,
    val senderAccountBalance: Long,
    val receiverAccountBalance: Long,
)
