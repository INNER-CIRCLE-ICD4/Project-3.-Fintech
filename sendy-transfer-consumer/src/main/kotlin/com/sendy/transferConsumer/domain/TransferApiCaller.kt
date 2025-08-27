package com.sendy.transferConsumer.domain

import com.sendy.transferDomain.domain.vo.TransferId
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(name = "transfer-internal-api", url = "\${feign.transfer.url}")
interface TransferApiCaller {
    @RequestMapping(method = [RequestMethod.PATCH], value = ["/reserve"])
    fun callReservedTransfer(transferIds: List<TransferId>)
}
