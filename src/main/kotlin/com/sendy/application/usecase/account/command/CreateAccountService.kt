package com.sendy.application.usecase.account.command

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.dto.account.CreateAccountResponse
import com.sendy.domain.account.AccountEntity
import com.sendy.domain.account.AccountRepository
import com.sendy.domain.account.AccountStatus
import com.sendy.support.util.getTsid
import com.sendy.support.util.AccountNumberValidator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class CreateAccountService(
    private val accountRepository: AccountRepository,
    private val createAccountNumberUseCase: CreateAccountNumberUseCase
) : CreateAccountUseCase {
    override fun execute(request: CreateAccountRequest): CreateAccountResponse {
        // 서버에서 계좌번호를 자동 생성
        val generatedAccountNumber = createAccountNumberUseCase.generate()
        
        // 생성된 계좌번호 형식 검증 (321-xxxx-xx-xxxx 형식)
        AccountNumberValidator.validateFormat(generatedAccountNumber)

        return accountRepository
            .save(
                AccountEntity(
                    id = getTsid(),
                    accountNumber = generatedAccountNumber, // 서버에서 생성한 계좌번호 사용
                    userId = request.userId,
                    password = request.password, // 클라이언트에서 받은 비밀번호
                    status = AccountStatus.ACTIVE,
                    isPrimary = request.isPrimary,
                    isLimitedAccount = request.isLimitedAccount,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                    balance = 0L,
                ),
            ).let {
                CreateAccountResponse(it.accountNumber)
            }
    }
}
