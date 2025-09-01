package com.sendy.sendyLegacyApi.application.usecase.transfer

import com.sendy.sendyLegacyApi.application.dto.transfer.ReservedTransferCommand
import com.sendy.sendyLegacyApi.domain.transfer.TransferRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReservedTransferSendMoneyService(
    private val transferRepository: TransferRepository,
    private val transferHistoryProcessor: TransferHistoryProcessor,
    private val transferProcessor: TransferProcessor,
) {
    fun reservedTransferMoney(command: ReservedTransferCommand) {
        val transferEntities = command.transferIds.mapNotNull { transferRepository.findByIdOrNull(it.transferId) }

        transferEntities.forEach {
            it.changeToPending()
        }

        transferRepository.saveAll(transferEntities)

        transferEntities.forEach { transfer ->
            runCatching {
                transferProcessor.doTransferNoPassword(transfer)
            }.onSuccess {
                transferHistoryProcessor.saveSuccess(transfer)
                // TODO. outbox 테이블 저장(성공)
            }.onFailure {
                transferHistoryProcessor.saveFail(transfer)
                // TODO. outbox 테이블 저장(실패)
            }
        }
    }
}
