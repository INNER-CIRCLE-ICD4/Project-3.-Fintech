package com.sendy.transferScheduler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SendyTransferSchedulerApplication

fun main(vararg args: String) {
    runApplication<SendyTransferSchedulerApplication>(*args)
}
