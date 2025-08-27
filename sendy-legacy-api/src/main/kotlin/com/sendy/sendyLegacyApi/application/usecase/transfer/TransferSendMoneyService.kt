package com.sendy.sendyLegacyApi.application.usecase.transfer

import com.sendy.sendyLegacyApi.application.dto.transfer.TransferMoneyCommand
import com.sendy.sendyLegacyApi.application.dto.transfer.TransferMoneyResponse
import com.sendy.sendyLegacyApi.application.usecase.transfer.command.TransferMoneyUseCase
import org.springframework.stereotype.Service

@Service
class TransferSendMoneyService(
    private val transferHistoryProcessor: TransferHistoryProcessor,
    private val transferProcessor: TransferProcessor,
) : TransferMoneyUseCase {
    override fun transferMoney(command: TransferMoneyCommand): TransferMoneyResponse {
        val transfer =
            transferHistoryProcessor.savePending(
                amount = command.amount,
                requestedAt = command.requestedAt,
                sendUserId = command.sendUserId,
                sendAccountNumber = command.sendAccountNumber,
                receivePhoneNumber = command.receivePhoneNumber,
            )

        runCatching {
            transferProcessor.doTransferWithPassword(command)
        }.onSuccess {
            this.transferHistoryProcessor.saveSuccess(transfer)
        }.onFailure {
            this.transferHistoryProcessor.saveFail(transfer)
            throw it
        }

        return TransferMoneyResponse(transfer.status)
    }
}
