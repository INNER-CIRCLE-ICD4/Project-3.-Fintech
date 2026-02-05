package com.sendy.sharedKafka.event.transfer

import java.math.BigDecimal

data class TransferInitiatedEvent(
    val transferId: String,
    val userId: Long,
    val fromAccountId: Long,
    val toAccountNumber: String,
    val toReceiverName: String, //받는이
    val amount: BigDecimal,
    val transferType: String,
    val description: String? = null
)
