package com.sendy.transferScheduler.config

import com.sendy.transferDomain.support.config.TransferDomainJdbcConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration(proxyBeanMethods = false)
@Import(value = [TransferDomainJdbcConfig::class])
@ComponentScan(value = ["com.sendy.transferDomain"])
class TestJdbcConfig
