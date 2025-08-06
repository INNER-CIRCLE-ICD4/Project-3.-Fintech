package com.sendy.interfaces.rest.transfer

import com.sendy.application.dto.transfer.TransferMoneyResponse
import com.sendy.application.usecase.transfer.command.TransferMoneyUseCase
import com.sendy.support.response.Response
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
        @RequestBody @Valid request: TransferMoneyToPhoneNumberCommand,
    ): Response<TransferMoneyResponse> = Response.ok(transferSendMoneyUseCase.transferMoney(request))
}
