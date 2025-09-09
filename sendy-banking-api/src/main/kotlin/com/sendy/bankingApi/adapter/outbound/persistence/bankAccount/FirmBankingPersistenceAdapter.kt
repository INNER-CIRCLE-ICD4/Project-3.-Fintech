package com.sendy.bankingApi.adapter.outbound.persistence.bankAccount

import com.sendy.bankingApi.application.outboud.bankAccount.RequestFirmBankingOutPort
import com.sendy.bankingApi.domain.bankAccount.RequestFirmBanking
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class FirmBankingPersistenceAdapter(
    private val firmBankingJpaRepository: FirmBankingJpaRepository,
) : RequestFirmBankingOutPort {
    override fun createRequestFirmBanking(requestFirmBanking: RequestFirmBanking): RequestFirmBanking {
        val save =
            firmBankingJpaRepository.save(
                FirmBankingJpaEntity(
                    id = requestFirmBanking.id,
                    fromBankName = requestFirmBanking.fromBankName,
                    fromBankAccountNumber = requestFirmBanking.toBankAccountNumber,
                    toBankName = requestFirmBanking.toBankName,
                    toBankAccountNumber = requestFirmBanking.toBankAccountNumber,
                    moneyAmount = requestFirmBanking.moneyAmount,
                    firmBankingStatus = requestFirmBanking.firmBankingStatus,
                ),
            )

        return RequestFirmBanking(
            id = save.id,
            fromBankName = save.fromBankName,
            fromBankAccountNumber = save.toBankAccountNumber,
            toBankName = save.toBankName,
            toBankAccountNumber = save.toBankAccountNumber,
            moneyAmount = save.moneyAmount,
            firmBankingStatus = save.firmBankingStatus,
        )
    }

    override fun updateRequestFirmBanking(requestFirmBanking: RequestFirmBanking): RequestFirmBanking {
        val firmBankingEntity = firmBankingJpaRepository.findByIdOrNull(requestFirmBanking.id)!!

        firmBankingEntity.firmBankingStatus = requestFirmBanking.firmBankingStatus

        firmBankingJpaRepository.save(firmBankingEntity)

        return requestFirmBanking
    }
}
