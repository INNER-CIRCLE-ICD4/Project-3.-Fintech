package com.sendy.sendyLegacyApi.application.usecase.account

import com.sendy.sendyLegacyApi.application.dto.account.AccountBalanceResponse
import com.sendy.sendyLegacyApi.application.usecase.account.query.ReadAccountBalanceUseCase
import com.sendy.sendyLegacyApi.domain.account.AccountRepository
import com.sendy.sendyLegacyApi.domain.account.AccountStatus
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class ReadAccountBalanceService(
    private val accountRepository: AccountRepository,
) : ReadAccountBalanceUseCase {
    override fun execute(
        userId: Long,
        accountNumber: String,
    ): AccountBalanceResponse =
        accountRepository
            .findByUserIdAndAccountNumber(userId, accountNumber)
            ?.let {
                check(it.status != AccountStatus.ACTIVE)
                AccountBalanceResponse(accountNumber = it.accountNumber, balance = it.balance)
            } ?: throw EntityNotFoundException("계좌를 찾을 수 없습니다.")
}
