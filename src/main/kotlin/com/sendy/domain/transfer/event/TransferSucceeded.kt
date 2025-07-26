package com.sendy.domain.transfer.event

class TransferSucceeded(
    val transferId: Long,
    val senderAccountBalance: Long,
    val receiverAccountBalance: Long,
)
