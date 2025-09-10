package com.sendy.bankingApi.application.service

import com.sendy.bankingApi.application.inbound.money.IncreaseMoneyChangingInPort
import com.sendy.bankingApi.application.outboud.bankAccount.BankAccountOutPort
import com.sendy.bankingApi.application.outboud.bankAccount.RequestExternalBankAccountAmountInfoOutPort
import com.sendy.bankingApi.application.outboud.bankAccount.RequestExternalFirmBankingOutPort
import com.sendy.bankingApi.application.outboud.bankAccount.RequestInternalUserInfoOutPort
import com.sendy.bankingApi.application.outboud.money.IncreaseMoneyChangingOutPort
import com.sendy.bankingApi.application.outboud.money.MoneyChangingOutPort
import com.sendy.bankingApi.domain.money.RequestMoneyChanging
import com.sendy.bankingApi.support.util.getTSID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MoneyChangingService(
    private val increaseMoneyChangingOutPort: IncreaseMoneyChangingOutPort,
    private val bankAccountOutPort: BankAccountOutPort,
    private val requestExternalBankAccountAmountInfoOutPort: RequestExternalBankAccountAmountInfoOutPort,
    private val requestInternalUserInfoOutPort: RequestInternalUserInfoOutPort,
    private val requestExternalFirmBankingOutPort: RequestExternalFirmBankingOutPort,
    private val moneyChangingOutPort: MoneyChangingOutPort,
) : IncreaseMoneyChangingInPort {
    @Transactional
    override fun increaseMoneyChanging(
        command: IncreaseMoneyChangingInPort.IncreaseMoneyChangingCommand,
    ): IncreaseMoneyChangingInPort.IncreaseMoneyChangingResult {
        // 머니 충전 증액 과정

        // 1. 고객 정보 유효성 체크(sendy-legacy-api)
        val userInfo = requestInternalUserInfoOutPort.requestExternalUserInfo(command.targetUserId)
        if (userInfo.isDelete) {
            throw RuntimeException("user deleted")
        }

        // 2. 고객의 연동된 계좌 유효성 체크(bankingRepository)
        val bankAccount = bankAccountOutPort.getBankAccountByUserId(command.targetUserId)
        if (!bankAccount.linkedStatusIsValid) {
            throw RuntimeException("user bank account is not valid")
        }

        // 2-1. 고객의 연동된 계좌의 잔액이 충분한지 유효성 체크(external bankAccount)
        val amountInfo =
            requestExternalBankAccountAmountInfoOutPort.requestExternalBankAccountAmountInfo(
                RequestExternalBankAccountAmountInfoOutPort.RequestExternalBankAccountAmountInfoRequestDto(
                    bankName = bankAccount.bankName,
                    bankAccountNumber = bankAccount.bankAccountNumber,
                ),
            )
        if (amountInfo.amount < command.amount) {
            throw RuntimeException("real bank account amount is not enough")
        }

        // 3. 법인 계좌 유효성 체크(bankingRepository) -> ok

        // 4. 증액을 위한 "기록", 요청 상태로 생성(MoneyChanging)
        val increaseMoney =
            increaseMoneyChangingOutPort.increaseMoney(
                RequestMoneyChanging(
                    id = getTSID(),
                    targetUserId = command.targetUserId,
                    changingType = 1,
                    changingMoneyStatus = 0,
                    changingMoneyAmount = command.amount,
                ),
            )

        // 4-1. 펌뱀킹을 수행하고 고객의 연동된 계좌 -> sendy 법인 계좌(bankingRepository) -> ok
        val requestExternalFirmBanking =
            requestExternalFirmBankingOutPort.requestExternalFirmBanking(
                RequestExternalFirmBankingOutPort.RequestExternalRequestDto(
                    fromBankName = bankAccount.bankName,
                    fromBankAccountNumber = bankAccount.bankAccountNumber,
                    toBankName = "sendy-corp",
                    toBankAccountNumber = "3211234123411",
                    moneyAmount = command.amount,
                ),
            )

        val requestMoneyChanging =
            if (requestExternalFirmBanking.resultCode == 0) {
                // 5. 결과 정상적이라면 성공 상태값 변경
                increaseMoney.copy(changingMoneyStatus = 1)
            } else {
                // 6. 결과가 실패라면 실패 상태값 변경
                increaseMoney.copy(changingMoneyStatus = 2)
            }

        moneyChangingOutPort.updateMoneyChanging(requestMoneyChanging)

        return IncreaseMoneyChangingInPort.IncreaseMoneyChangingResult(
            id = requestMoneyChanging.id,
            targetUserId = requestMoneyChanging.targetUserId.value,
            changingType = requestMoneyChanging.changingType,
            changingMoneyAmount = requestMoneyChanging.changingMoneyAmount,
            changingMoneyStatus = requestMoneyChanging.changingMoneyStatus,
            createdAt = requestMoneyChanging.createdAt,
        )
    }
}
