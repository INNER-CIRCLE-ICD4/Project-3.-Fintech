package com.sendy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SendyApplication

fun main(args: Array<String>) {
    runApplication<SendyApplication>(*args)
}
