package com.sendy.application.usecase.account.query

import com.sendy.application.dto.account.AccountBalanceResponse
import com.sendy.domain.account.AccountRepository
import com.sendy.domain.account.AccountStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class QueryAccountBalanceUseCase(
    private val accountRepository: AccountRepository
) {
    fun execute(userId: Long, accountNumber: String): AccountBalanceResponse {
        return accountRepository.findByUserIdAndAccountNumber(userId, accountNumber)?.let {
            if (it.status != AccountStatus.ACTIVE) throw IllegalStateException("계좌 상태가 ACTIVE가 아닙니다.")
            AccountBalanceResponse(it.accountNumber, it.balance)
        } ?: throw IllegalArgumentException("계좌를 찾을 수 없습니다.")
    }
}

