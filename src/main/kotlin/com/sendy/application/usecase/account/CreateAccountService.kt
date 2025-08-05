package com.sendy.application.usecase.account.command

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.dto.account.CreateAccountResponse
import com.sendy.application.usecase.account.CreateAccountEntityService
import com.sendy.domain.account.AccountRepository
import com.sendy.support.util.SHA256Util
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CreateAccountService(
    private val accountRepository: AccountRepository,
    private val generateAccountNumberUseCase: GeneratedAccountNumberUseCase,
    private val createAccountEntityService: CreateAccountEntityService,
    private val sha256Util: SHA256Util
) : CreateAccountUseCase {
    override fun execute(request: CreateAccountRequest): CreateAccountResponse {
        // 서버에서 계좌번호를 자동 생성
        // 1. 계좌번호 생성 및 검증
        val accountNumber = generateAccountNumberUseCase.execute()

        //추후에 usecase로 변경시 사용하겠습니다.
        // 2.비밀번호 SHA256 암호화 (양방향 암호화로 송금 시 검증 가능)
        val encryptedPassword = sha256Util.hash(request.password)

        //3. 계좌 엔티티 생성
        val accountEntity = createAccountEntityService.execute(request, accountNumber, encryptedPassword)

        //4. 계좌 저장
        val savedAccount = accountRepository.save(accountEntity)

        return CreateAccountResponse(savedAccount.accountNumber)
    }
}
