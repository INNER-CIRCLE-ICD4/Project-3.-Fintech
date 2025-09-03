package com.sendy.transferProducer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SendyTransferProducerApplication

fun main(args: Array<String>) {
    runApplication<SendyTransferProducerApplication>(*args)
}
