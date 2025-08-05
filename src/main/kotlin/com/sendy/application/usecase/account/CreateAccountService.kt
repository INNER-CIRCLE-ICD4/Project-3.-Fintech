package com.sendy.application.usecase.account

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.dto.account.CreateAccountResponse
import com.sendy.application.usecase.account.command.CreateAccountUseCase
import com.sendy.domain.account.AccountEntity
import com.sendy.domain.account.AccountNumberValidator
import com.sendy.domain.account.AccountRepository
import com.sendy.domain.account.AccountStatus
import com.sendy.support.util.SHA256Util
import com.sendy.support.util.getTsid
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class CreateAccountService(
    private val accountRepository: AccountRepository,
    private val sha256Util: SHA256Util,
) : CreateAccountUseCase {
    override fun execute(request: CreateAccountRequest): CreateAccountResponse {
        // 1. 계좌번호 생성 및 검증
        val accountNumber = AccountEntity.generateAccountNumber()

        // 2. 계좌 검증
        AccountNumberValidator.validateFormat(accountNumber)

        // 2.비밀번호 SHA256 암호화 (단방향 암호화로 송금 시 검증 가능)
        val encryptedPassword = sha256Util.hash(request.password)

        // 3. 계좌 엔티티 생성
        val accountEntity =
            AccountEntity(
                id = getTsid(),
                accountNumber = accountNumber,
                userId = request.userId,
                password = encryptedPassword,
                status = AccountStatus.ACTIVE,
                isPrimary = request.isPrimary,
                isLimitedAccount = request.isLimitedAccount,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                balance = request.initBalance,
            )

        // 4. 계좌 저장
        val savedAccount = accountRepository.save(accountEntity)

        return CreateAccountResponse(savedAccount.accountNumber)
    }
}
