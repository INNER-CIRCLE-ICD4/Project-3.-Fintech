package com.sendy.transferConsumer.infrastructure.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["com.sendy.transferConsumer.domain"])
class FeignClientConfig
