package com.sendy.sendyLegacyApi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class SendyLegacyApi

fun main(args: Array<String>) {
    runApplication<SendyLegacyApi>(*args)
}
