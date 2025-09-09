package com.sendy.bankingApi.application.service

import com.sendy.bankingApi.application.inbound.bankAccount.RequestFirmBankingInPort
import com.sendy.bankingApi.application.outboud.bankAccount.RequestExternalFirmBankingOutPort
import com.sendy.bankingApi.application.outboud.bankAccount.RequestFirmBankingOutPort
import com.sendy.bankingApi.domain.bankAccount.RequestFirmBanking
import com.sendy.bankingApi.domain.vo.FirmBankingId
import com.sendy.bankingApi.support.util.getTSID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FirmBankingService(
    private val requestFirmBankingOutPort: RequestFirmBankingOutPort,
    private val requestExternalFirmBankingOutPort: RequestExternalFirmBankingOutPort,
) : RequestFirmBankingInPort {
    @Transactional
    override fun requestFirmBanking(command: RequestFirmBankingInPort.RequestFirmBankingCommand): FirmBankingId {
        // from -> to 계좌로 입/출금 진행

        // 먼저 상태가 '요청'인 부분으로 저장
        val requestFirmBanking =
            RequestFirmBanking(
                id = getTSID(),
                fromBankName = command.fromBankName,
                fromBankAccountNumber = command.fromBankAccountNumber,
                toBankName = command.toBankName,
                toBankAccountNumber = command.toBankAccountNumber,
                moneyAmount = command.moneyAccount,
                firmBankingStatus = 0,
            )

        val createRequestFirmBanking = requestFirmBankingOutPort.createRequestFirmBanking(requestFirmBanking)

        // 외부 은행 펌뱀킹 요청
        val requestExternalFirmBanking =
            requestExternalFirmBankingOutPort.requestExternalFirmBanking(
                RequestExternalFirmBankingOutPort.RequestExternalRequestDto(
                    fromBankName = command.fromBankName,
                    fromBankAccountNumber = command.fromBankAccountNumber,
                    toBankName = command.toBankName,
                    toBankAccountNumber = command.toBankAccountNumber,
                    moneyAmount = command.moneyAccount,
                ),
            )

        // 상태 업데이트
        val updateFirmBanking =
            if (requestExternalFirmBanking.resultCode == 0) {
                createRequestFirmBanking.copy(firmBankingStatus = 1)
            } else {
                createRequestFirmBanking.copy(firmBankingStatus = 2)
            }

        requestFirmBankingOutPort.updateRequestFirmBanking(updateFirmBanking)

        // 결과 리턴
        return FirmBankingId(updateFirmBanking.id)
    }
}
