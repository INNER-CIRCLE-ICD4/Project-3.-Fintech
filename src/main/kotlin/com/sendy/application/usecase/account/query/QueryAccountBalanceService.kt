package com.sendy.application.usecase.account.query

import com.sendy.domain.account.AccountEntity
import com.sendy.domain.account.AccountRepository
import com.sendy.domain.account.AccountStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class QueryAccountBalanceService(
    private val accountRepository: AccountRepository,
) : QueryAccountBalanceUseCase {
    override fun execute(
        userId: Long,
        accountNumber: String,
    ): AccountEntity =
        accountRepository.findByUserIdAndAccountNumber(userId, accountNumber)?.let {
            if (it.status != AccountStatus.ACTIVE) throw IllegalStateException("계좌 상태가 ACTIVE가 아닙니다.")
            it
        } ?: throw IllegalArgumentException("계좌를 찾을 수 없습니다.")
}
