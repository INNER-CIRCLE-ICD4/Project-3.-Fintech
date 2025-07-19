package com.sendy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com"])
class SendyApplication

fun main(args: Array<String>) {
    runApplication<SendyApplication>(*args)
}
