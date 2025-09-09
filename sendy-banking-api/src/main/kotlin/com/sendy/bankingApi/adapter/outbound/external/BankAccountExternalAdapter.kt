package com.sendy.bankingApi.adapter.outbound.external

import com.sendy.bankingApi.application.outboud.bankAccount.RetrieveBankAccountInfoOutPort
import org.springframework.stereotype.Component

@Component
class BankAccountExternalAdapter : RetrieveBankAccountInfoOutPort {
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
}
