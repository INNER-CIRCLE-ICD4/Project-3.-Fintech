package com.sendy.infrastructure.config

import com.sendy.domain.account.TransactionHistoryRepository
import com.sendy.domain.transfer.TransferLimitRepository
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackageClasses = [TransferLimitRepository::class, TransactionHistoryRepository::class])
@EntityScan(basePackages = ["com.sendy.domain.transfer", "com.sendy.domain.account"])
class TestTransferProcessorConfig
