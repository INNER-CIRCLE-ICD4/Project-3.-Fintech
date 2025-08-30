package com.sendy.transferConsumer.interfaces.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sendy.transferConsumer.domain.ReservedRequestDto
import com.sendy.transferConsumer.domain.TransferApiCaller
import com.sendy.transferDomain.domain.vo.TransferId
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ReservedTransferListener(
    private val transferApiClient: TransferApiCaller,
    private val objectMapper: ObjectMapper,
) {
    @KafkaListener(topics = ["transfer-scheduler.transfer.reservation.started"])
    fun transferReservationStartedListener(payload: String) {
        val data = objectMapper.readValue<List<TransferId>>(payload)
        transferApiClient.callReservedTransfer(ReservedRequestDto(data))
    }
}
