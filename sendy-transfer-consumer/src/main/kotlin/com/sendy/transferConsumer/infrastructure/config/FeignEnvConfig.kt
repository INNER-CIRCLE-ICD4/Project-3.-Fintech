package com.sendy.transferConsumer.infrastructure.config

import feign.Logger
import org.springframework.context.annotation.Bean

class FeignEnvConfig {
    @Bean
    fun feignLogLevel(): Logger.Level = Logger.Level.FULL
}
