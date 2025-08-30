package com.sendy.transferConsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SendyTransferConsumerApplication

fun main(args: Array<String>) {
    runApplication<SendyTransferConsumerApplication>(*args)
}
