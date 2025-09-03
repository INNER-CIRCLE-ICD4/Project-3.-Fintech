package com.sendy.sharedKafka.event.transfer

data class TransferSucceed(
    val deposit: TransferInfo,
    val withdraw: TransferInfo,
) {
    data class TransferInfo(
        val userId: Long,
        val amount: Long,
    )
}
