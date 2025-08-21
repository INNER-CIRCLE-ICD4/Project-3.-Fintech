package com.sendy.transferConsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.KafkaListener

@SpringBootApplication
class SendyTransferConsumerApplication {
    @KafkaListener(topics = ["transfer-scheduler.transfer.reservation.started"])
    fun transferReservationStartedListener() {
        println("a")
    }
}

fun main(args: Array<String>) {
    runApplication<SendyTransferConsumerApplication>(*args)
}
