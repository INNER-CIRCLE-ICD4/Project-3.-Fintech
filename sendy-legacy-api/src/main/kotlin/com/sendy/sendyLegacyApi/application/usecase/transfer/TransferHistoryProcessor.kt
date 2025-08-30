package com.sendy.sendyLegacyApi.application.usecase.transfer

import com.sendy.sendyLegacyApi.domain.enum.TransferStatusEnum
import com.sendy.sendyLegacyApi.domain.transfer.TransferEntity
import com.sendy.sendyLegacyApi.domain.transfer.TransferRepository
import com.sendy.sendyLegacyApi.support.util.getTsid
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class TransferHistoryProcessor(
    private val transferRepository: TransferRepository,
) {
    @Transactional
    fun savePending(
        amount: Long,
        requestedAt: LocalDateTime,
        sendUserId: Long,
        sendAccountNumber: String,
        receivePhoneNumber: String?,
    ): TransferEntity =
        transferRepository.save(
            TransferEntity(
                id = getTsid(),
                amount = amount,
                status = TransferStatusEnum.PENDING,
                requestedAt = requestedAt,
                sendUserId = sendUserId,
                sendAccountNumber = sendAccountNumber,
                receivePhoneNumber = receivePhoneNumber,
            ),
        )

    @Transactional
    fun saveFail(transfer: TransferEntity) {
        transfer.changeToFail()

        transferRepository.save(transfer)
    }

    @Transactional
    fun saveSuccess(transfer: TransferEntity) {
        transfer.changeToSuccess()

        transferRepository.save(transfer)
    }
}
