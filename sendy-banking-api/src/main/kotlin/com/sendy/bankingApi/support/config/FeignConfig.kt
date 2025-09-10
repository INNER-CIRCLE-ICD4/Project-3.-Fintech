package com.sendy.bankingApi.support.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(basePackages = ["com.sendy.bankingApi.adapter.outbound.internal"])
class FeignConfig
