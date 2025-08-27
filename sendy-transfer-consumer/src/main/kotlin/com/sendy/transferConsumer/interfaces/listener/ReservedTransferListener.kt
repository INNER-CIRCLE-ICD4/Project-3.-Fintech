package com.sendy.transferConsumer.interfaces.listener

import com.sendy.transferConsumer.domain.TransferApiCaller
import com.sendy.transferDomain.domain.vo.TransferId
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ReservedTransferListener(
    private val transferApiClient: TransferApiCaller,
) {
    @KafkaListener(topics = ["transfer-scheduler.transfer.reservation.started"])
    fun transferReservationStartedListener(data: List<TransferId>) {
        transferApiClient.callReservedTransfer(data)
    }
}
