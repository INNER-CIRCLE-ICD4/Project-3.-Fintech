package com.sendy.bankingApi.adapter.outbound.external

import com.sendy.bankingApi.application.outboud.bankAccount.RequestExternalFirmBankingOutPort
import com.sendy.bankingApi.application.outboud.bankAccount.RetrieveBankAccountInfoOutPort
import org.springframework.stereotype.Component

@Component
class BankAccountExternalAdapter :
    RetrieveBankAccountInfoOutPort,
    RequestExternalFirmBankingOutPort {
    override fun retrieveBankAccountInfo(
        request: RetrieveBankAccountInfoOutPort.RetrieveBankAccountRequestDto,
    ): RetrieveBankAccountInfoOutPort.BankAccountInfoResponseDto {
        // 실제로 외부 은행과 통신(http)

        // 실제 은행 계좌 정보 조회

        // 정보 조회 후 BankAccountInfoResponseDto 파싱 후 리턴
        return RetrieveBankAccountInfoOutPort.BankAccountInfoResponseDto(
            bankName = request.bankName,
            bankAccountNumber = request.bankAccountNumber,
            isValid = true,
        )
    }

    override fun requestExternalFirmBanking(
        request: RequestExternalFirmBankingOutPort.RequestExternalRequestDto,
    ): RequestExternalFirmBankingOutPort.RequestExternalResponseDto {
        // 실제로 외부 은행과 통신(http)

        // 펌뱀킹 요청

        // 실제 결과를 리턴
        return RequestExternalFirmBankingOutPort.RequestExternalResponseDto(0)
    }
}
