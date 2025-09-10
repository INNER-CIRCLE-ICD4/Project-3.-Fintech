package com.sendy.bankingApi.adapter.outbound.persistence.money

import com.sendy.bankingApi.application.outboud.money.IncreaseMoneyChangingOutPort
import com.sendy.bankingApi.application.outboud.money.MoneyChangingOutPort
import com.sendy.bankingApi.domain.money.RequestMoneyChanging
import com.sendy.bankingApi.domain.vo.UserId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class MoneyChangingPersistenceAdapter(
    private val moneyChangingJpaRepository: MoneyChangingJpaRepository,
) : IncreaseMoneyChangingOutPort,
    MoneyChangingOutPort {
    override fun increaseMoney(requestMoneyChanging: RequestMoneyChanging): RequestMoneyChanging {
        val save =
            moneyChangingJpaRepository.save(
                MoneyChangingJpaEntity(
                    id = requestMoneyChanging.id,
                    targetUserId = requestMoneyChanging.targetUserId.value,
                    moneyChangingType = requestMoneyChanging.changingType,
                    changingMoneyStatus = requestMoneyChanging.changingMoneyStatus,
                    moneyAmount = requestMoneyChanging.changingMoneyAmount,
                ),
            )

        return RequestMoneyChanging(
            id = save.id,
            targetUserId = UserId(save.targetUserId),
            changingType = save.moneyChangingType,
            changingMoneyStatus = save.changingMoneyStatus,
            changingMoneyAmount = save.moneyAmount,
            createdAt = save.createdAt,
        )
    }

    override fun updateMoneyChanging(requestMoneyChanging: RequestMoneyChanging) {
        val entity = moneyChangingJpaRepository.findByIdOrNull(requestMoneyChanging.id)!!

        entity.changingMoneyStatus = requestMoneyChanging.changingMoneyStatus

        moneyChangingJpaRepository.save(entity)
    }
}
