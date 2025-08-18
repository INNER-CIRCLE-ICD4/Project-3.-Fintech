package com.sendy.sendyLegacyApi.application.usecase.transfer.config

import com.sendy.sendyLegacyApi.domain.account.AccountRepository
import com.sendy.sendyLegacyApi.domain.auth.UserEntityRepository
import com.sendy.sendyLegacyApi.domain.transfer.TransferRepository
import com.sendy.sendyLegacyApi.support.util.Aes256Util
import com.sendy.sendyLegacyApi.support.util.SHA256Util
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration(proxyBeanMethods = false)
@EnableAutoConfiguration
@EnableJpaRepositories(
    basePackageClasses = [
        UserEntityRepository::class,
        AccountRepository::class,
        TransferRepository::class,
    ],
)
@EntityScan(basePackages = ["com.sendy.sendyLegacyApi"])
class ReserveTransferConfig {
    @Bean
    fun sha256Util(): SHA256Util = SHA256Util()

    @Bean
    fun aes256Util() = Aes256Util("d2f3de5ce2bef6aad4c2b01e2bffdcd6")
}
