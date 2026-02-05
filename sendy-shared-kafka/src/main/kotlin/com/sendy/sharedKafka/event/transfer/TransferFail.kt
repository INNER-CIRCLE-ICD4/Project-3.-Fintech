package com.sendy.sharedKafka.event.transfer

data class TransferFail(
    val sendUserId: Long,
    val amount: Long,
)
