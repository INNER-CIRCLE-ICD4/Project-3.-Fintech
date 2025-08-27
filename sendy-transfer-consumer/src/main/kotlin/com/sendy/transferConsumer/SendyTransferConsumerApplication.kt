package com.sendy.transferConsumer

import com.sendy.transferDomain.domain.vo.TransferId
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.kafka.annotation.KafkaListener
import java.net.URI

@SpringBootApplication
class SendyTransferConsumerApplication {
    @KafkaListener(topics = ["transfer-scheduler.transfer.reservation.started"])
    fun transferReservationStartedListener(data: List<TransferId>) {
        val restTemplate = RestTemplateBuilder().build()

        val uri = URI.create("http://localhost:8080/inner/transfer")
        println("a")
    }
}

fun main(args: Array<String>) {
    runApplication<SendyTransferConsumerApplication>(*args)
}
