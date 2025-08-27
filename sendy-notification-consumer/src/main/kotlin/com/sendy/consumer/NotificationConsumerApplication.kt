package com.sendy.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.sendy.consumer", "com.sendy.sharedKafka", "com.sendy.sharedMongoDB"])
class NotificationConsumerApplication

fun main(args: Array<String>) {
    runApplication<NotificationConsumerApplication>(*args)
}