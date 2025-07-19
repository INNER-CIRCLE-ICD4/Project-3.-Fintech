package com.sendy.infrastructure.persistence.transfer.event

class TransferSucceeded(
    val transferId: Long,
    val senderAccountBalance: Long,
    val receiverAccountBalance: Long,
)
