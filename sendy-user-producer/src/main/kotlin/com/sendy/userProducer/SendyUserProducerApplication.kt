package com.sendy.userProducer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SendyUserProducerApplication

fun main(args: Array<String>) {
    runApplication<SendyUserProducerApplication>(*args)
}
