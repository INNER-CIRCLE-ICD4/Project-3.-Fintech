package com.sendy.transferConsumer.domain

import com.sendy.transferConsumer.infrastructure.config.FeignEnvConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "transfer-internal-api", url = "\${feign.transfer.url}", configuration = [FeignEnvConfig::class])
interface TransferApiCaller {
    @PostMapping("/reserve", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun callReservedTransfer(
        @RequestBody(required = true) request: ReservedRequestDto,
    )
}
