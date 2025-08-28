package com.sendy.sendyLegacyApi.interfaces.rest.transfer

import com.sendy.sendyLegacyApi.application.dto.transfer.ReservedTransferCommand
import com.sendy.sendyLegacyApi.application.dto.transfer.TransferId
import com.sendy.sendyLegacyApi.application.usecase.transfer.ReservedTransferSendMoneyService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/inner/transfers/reserve")
class TransferInnerController(
    private val reservedTransferSendMoneyService: ReservedTransferSendMoneyService,
) {
    @PostMapping
    fun reservedTransferSendMoney(
        @RequestBody @Valid request: ReservedTransferRequestDto,
    ) {
        val command = ReservedTransferCommand(request.transferIds.map { TransferId(it.value) })
        reservedTransferSendMoneyService.reservedTransferMoney(command)
    }
}
