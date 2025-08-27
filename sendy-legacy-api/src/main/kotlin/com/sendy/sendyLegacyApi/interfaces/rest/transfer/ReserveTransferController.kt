package com.sendy.sendyLegacyApi.interfaces.rest.transfer

import com.sendy.sendyLegacyApi.application.dto.transfer.ReserveTransferCommand
import com.sendy.sendyLegacyApi.application.usecase.transfer.command.ReserveTransferUseCase
import com.sendy.sendyLegacyApi.support.response.Response
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("transfer/reserve")
class ReserveTransferController(
    private val reserveTransferUseCase: ReserveTransferUseCase,
) {
    @PostMapping
    fun reserveTransfer(
        @RequestBody @Valid request: ReserveTransferCommand,
    ) = reserveTransferUseCase.reserveTransfer(request).let {
        Response.ok(it)
    }
}
