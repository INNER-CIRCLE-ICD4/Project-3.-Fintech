package com.sendy.bankingApi.application.service

import com.sendy.bankingApi.application.inbound.bankAccount.RegisterBankAccountInPort
import com.sendy.bankingApi.application.outboud.bankAccount.RegisterBankAccountOutPort
import com.sendy.bankingApi.application.outboud.bankAccount.RetrieveBankAccountInfoOutPort
import com.sendy.bankingApi.domain.bankAccount.RegisterBankAccount
import com.sendy.bankingApi.domain.vo.BankAccountId
import com.sendy.bankingApi.support.util.getTSID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BankAccountService(
    private val registerBankAccountOutPort: RegisterBankAccountOutPort,
    private val retrieveBankAccountInfoOutPort: RetrieveBankAccountInfoOutPort,
) : RegisterBankAccountInPort {
    @Transactional
    override fun registerBankAccount(command: RegisterBankAccountInPort.RegisterBankAccountCommand): BankAccountId? {
        // command.userId 유효성 체크
        // openfeign 을 이용해서 sendy-legacy-api 로 동기 호출 진행

        // 외부 은행 계좌를 등록 하는 서비스

        // 1. 외부 실제 은행에 등록이 가능한 계좌인지 검사한다.

        // 2. 외부의 은행에 이 계좌가 정상인지 확인
        // 외부 은행과 외부 통신 진행
        // Port -> Adapter -> 외부 통신
        val retrieveBankAccountInfo =
            retrieveBankAccountInfoOutPort.retrieveBankAccountInfo(
                RetrieveBankAccountInfoOutPort.RetrieveBankAccountRequestDto(
                    bankName = command.bankName,
                    bankAccountNumber = command.bankAccountNumber,
                ),
            )

        return if (retrieveBankAccountInfo.isValid) {
            val createRegisterBankAccount =
                registerBankAccountOutPort.createRegisterBankAccount(
                    RegisterBankAccount(
                        id = getTSID(),
                        bankName = command.bankName,
                        bankAccountNumber = command.bankAccountNumber,
                        userId = command.userId,
                        linkedStatusIsValid = true,
                    ),
                )

            BankAccountId(createRegisterBankAccount)
        } else {
            null
        }
    }
}
