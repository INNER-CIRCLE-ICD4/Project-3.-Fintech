package com.sendy.application.usecase.account.command

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.dto.account.CreateAccountResponse
import com.sendy.domain.account.AccountEntity
import com.sendy.domain.account.AccountStatus
import com.sendy.infrastructure.persistence.account.AccountRepository
import com.sendy.support.util.getTsid
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class CreateAccountService(
    private val accountRepository: AccountRepository
) : CreateAccountUseCase {
    override fun execute(request: CreateAccountRequest): CreateAccountResponse {
        require(request.accountNumber.length == 13) { "계좌번호는 13자리여야 합니다." }
        require(request.accountNumber.all { it.isDigit() }) { "계좌번호는 숫자만 가능합니다." }

        return accountRepository.save(
            AccountEntity(
                id = getTsid(),
                accountNumber = request.accountNumber,
                userId = request.userId,
                status = AccountStatus.ACTIVE,
                isPrimary = request.isPrimary,
                isLimitedAccount = request.isLimitedAccount,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                balance = 0L
            )
        ).let {
            CreateAccountResponse(it.accountNumber)
        }
    }
}

