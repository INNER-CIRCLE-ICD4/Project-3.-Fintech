package com.sendy.sendyLegacyApi.interfaces.rest.transfer

import com.sendy.sendyLegacyApi.application.dto.transfer.ReservedTransferCommand
import com.sendy.sendyLegacyApi.application.usecase.transfer.ReservedTransferSendMoneyService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/transfers/reserve")
class TransferInnerController(
    private val reservedTransferSendMoneyService: ReservedTransferSendMoneyService,
) {
    @PatchMapping
    fun reservedTransferSendMoney(
        @RequestBody @Valid request: ReservedTransferCommand,
    ) {
        reservedTransferSendMoneyService.reservedTransferMoney(request)
    }
}
