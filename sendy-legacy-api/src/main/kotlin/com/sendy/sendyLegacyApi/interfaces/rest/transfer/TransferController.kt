package com.sendy.sendyLegacyApi.interfaces.rest.transfer

import com.sendy.sendyLegacyApi.application.dto.transfer.TransferMoneyCommand
import com.sendy.sendyLegacyApi.application.dto.transfer.TransferMoneyResponse
import com.sendy.sendyLegacyApi.application.usecase.transfer.command.TransferMoneyUseCase
import com.sendy.sendyLegacyApi.support.response.Response
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("transfers")
class TransferController(
    private val transferSendMoneyUseCase: TransferMoneyUseCase,
) {
    @PostMapping
    fun transferSendMoney(
        @RequestBody @Valid request: TransferMoneyCommand,
    ): Response<TransferMoneyResponse> = Response.ok(transferSendMoneyUseCase.transferMoney(request))
}
