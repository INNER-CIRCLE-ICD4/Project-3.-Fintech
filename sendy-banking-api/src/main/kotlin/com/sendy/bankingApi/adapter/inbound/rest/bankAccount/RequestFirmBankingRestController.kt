package com.sendy.bankingApi.adapter.inbound.rest.bankAccount

import com.sendy.bankingApi.adapter.inbound.rest.bankAccount.dto.RequestFirmBankingRequestDto
import com.sendy.bankingApi.application.inbound.bankAccount.RequestFirmBankingInPort
import com.sendy.bankingApi.domain.vo.FirmBankingId
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bank-accounts/firm-banking")
class RequestFirmBankingRestController(
    private val requestFirmBankingInPort: RequestFirmBankingInPort,
) {
    @PostMapping
    fun requestFirmBanking(
        @RequestBody @Valid request: RequestFirmBankingRequestDto,
    ): ResponseEntity<FirmBankingId> {
        val requestFirmBanking =
            requestFirmBankingInPort.requestFirmBanking(
                RequestFirmBankingInPort.RequestFirmBankingCommand(
                    fromBankName = request.fromBankName,
                    fromBankAccountNumber = request.fromBankAccountNumber,
                    toBankName = request.toBankName,
                    toBankAccountNumber = request.toBankAccountNumber,
                    moneyAccount = request.moneyAccount,
                ),
            )

        return ResponseEntity.ok(requestFirmBanking)
    }
}
