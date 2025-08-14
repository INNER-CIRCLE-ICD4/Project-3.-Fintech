package com.sendy.sendyLegacyApi.infrastructure.config

import com.sendy.sendyLegacyApi.domain.account.TransactionHistoryRepository
import com.sendy.sendyLegacyApi.domain.transfer.TransferLimitRepository
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackageClasses = [TransferLimitRepository::class, TransactionHistoryRepository::class])
@EntityScan(basePackages = ["com.sendy.sendyLegacyApi"])
class TestTransferProcessorConfig
