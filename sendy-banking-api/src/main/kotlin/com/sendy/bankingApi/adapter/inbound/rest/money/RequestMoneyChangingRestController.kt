package com.sendy.bankingApi.adapter.inbound.rest.money

import com.sendy.bankingApi.adapter.inbound.rest.money.dto.IncreaseMoneyChangingRequestDto
import com.sendy.bankingApi.adapter.inbound.rest.money.dto.MoneyChangingResultDetail
import com.sendy.bankingApi.application.inbound.money.IncreaseMoneyChangingInPort
import com.sendy.bankingApi.domain.vo.UserId
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("money")
class RequestMoneyChangingRestController(
    private val increaseMoneyChangingInPort: IncreaseMoneyChangingInPort,
) {
    @PostMapping("/increase")
    fun increaseMoneyChanging(
        @RequestBody @Valid request: IncreaseMoneyChangingRequestDto,
    ): ResponseEntity<MoneyChangingResultDetail> {
        val increaseMoneyChanging =
            increaseMoneyChangingInPort.increaseMoneyChanging(
                IncreaseMoneyChangingInPort.IncreaseMoneyChangingCommand(
                    targetUserId = UserId(request.targetUserId),
                    amount = request.amount,
                ),
            )

        val (
            id,
            _,
            changingType,
            changingMoneyAmount,
            changingMoneyStatus,
        ) = increaseMoneyChanging

        return ResponseEntity.ok(
            MoneyChangingResultDetail(
                id = id,
                moneyChangingType = MoneyChangingResultDetail.MoneyChangingType.entries[changingType],
                amount = changingMoneyAmount,
                moneyChangingResultStatus = MoneyChangingResultDetail.MoneyChangingResultStatus.entries[changingMoneyStatus],
            ),
        )
    }
}
