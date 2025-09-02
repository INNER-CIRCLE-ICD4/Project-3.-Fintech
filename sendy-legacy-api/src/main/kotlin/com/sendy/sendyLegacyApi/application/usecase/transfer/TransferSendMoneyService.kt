package com.sendy.sendyLegacyApi.application.usecase.transfer

import com.sendy.sendyLegacyApi.application.dto.transfer.TransferMoneyCommand
import com.sendy.sendyLegacyApi.application.dto.transfer.TransferMoneyResponse
import com.sendy.sendyLegacyApi.application.usecase.transfer.command.TransferMoneyUseCase
import com.sendy.sendyLegacyApi.support.util.getTsid
import com.sendy.sharedKafka.domain.EventMessage
import com.sendy.sharedKafka.domain.EventMessageRepository
import com.sendy.sharedKafka.event.transfer.TransferFail
import com.sendy.sharedKafka.event.transfer.TransferSucceed
import com.sendy.sharedKafka.support.constant.EventTypes
import org.springframework.stereotype.Service

@Service
class TransferSendMoneyService(
    private val transferHistoryProcessor: TransferHistoryProcessor,
    private val transferProcessor: TransferProcessor,
    private val eventMessageRepository: EventMessageRepository,
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
            val doTransferWithPassword = transferProcessor.doTransferWithPassword(command)
            doTransferWithPassword
        }.onSuccess {
            this.transferHistoryProcessor.saveSuccess(transfer)
            this.eventMessageRepository.saveReady(
                EventMessage(
                    id = getTsid(),
                    aggregateId = transfer.id,
                    source = "sendy-legacy-api",
                    type = EventTypes.TRANSFER_SUCCEED,
                    payload =
                        TransferSucceed(
                            deposit =
                                TransferSucceed.TransferInfo(
                                    userId = it.deposit.userId,
                                    amount = command.amount,
                                ),
                            withdraw =
                                TransferSucceed.TransferInfo(
                                    userId = it.withdraw.userId,
                                    amount = command.amount,
                                ),
                        ),
                ),
            )
        }.onFailure {
            this.transferHistoryProcessor.saveFail(transfer)
            this.eventMessageRepository.saveReady(
                EventMessage(
                    id = getTsid(),
                    aggregateId = transfer.id,
                    source = "sendy-legacy-api",
                    type = EventTypes.TRANSFER_FAILED,
                    payload =
                        TransferFail(
                            sendUserId = command.sendUserId,
                            amount = command.amount,
                        ),
                ),
            )
            throw it
        }

        return TransferMoneyResponse(transfer.status)
    }
}
