package com.sendy.bankingApi.adapter.inbound.rest.bankAccount

import com.sendy.bankingApi.adapter.inbound.rest.bankAccount.dto.RegisterBankAccountRequestDto
import com.sendy.bankingApi.application.inbound.bankAccount.RegisterBankAccountInPort
import com.sendy.bankingApi.domain.vo.BankAccountId
import com.sendy.bankingApi.domain.vo.UserId
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("bank-accounts")
class RegisterBankAccountRestController(
    private val registerBankAccountInPort: RegisterBankAccountInPort,
) {
    @PostMapping
    fun registerBankAccount(
        @RequestBody @Valid request: RegisterBankAccountRequestDto,
    ): ResponseEntity<BankAccountId> {
        val registerBankAccount =
            registerBankAccountInPort.registerBankAccount(
                RegisterBankAccountInPort.RegisterBankAccountCommand(
                    bankName = request.bankName,
                    bankAccountNumber = request.bankAccountNumber,
                    userId = UserId(request.userId),
                ),
            ) ?: throw RuntimeException("등록 실패")

        return ResponseEntity.ok(registerBankAccount)
    }
}
