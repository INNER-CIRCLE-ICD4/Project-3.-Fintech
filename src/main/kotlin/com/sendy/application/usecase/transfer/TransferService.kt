package com.sendy.application.usecase.transfer

import com.sendy.application.dto.transfer.TransferMoneyCommand
import com.sendy.application.usecase.transfer.command.TransferMoneyUseCase
import com.sendy.domain.enum.TransferStatusEnum
import com.sendy.domain.transfer.TransactionHistoryRepository
import com.sendy.domain.transfer.TransferEntity
import com.sendy.domain.transfer.TransferRepository
import com.sendy.domain.transfer.event.TransferProcessed
import com.sendy.domain.transfer.event.TransferSucceeded
import com.sendy.support.util.getTsid
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransferService(
    private val transferRepository: TransferRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val publisher: ApplicationEventPublisher,
) : TransferMoneyUseCase {
    private val logger = LoggerFactory.getLogger(TransferService::class.java)

    @Transactional
    override fun transferMoney(command: TransferMoneyCommand) {
        // 이체 이력 PENDING 저장
        val transferId = getTsid()
        transferRepository.save(
            TransferEntity(
                id = transferId,
                amount = command.amount,
                status = TransferStatusEnum.PENDING,
                requestedAt = command.requestedAt,
            ),
        )

        // TODO. 2차 인증 호출

        // 임시저장, 계좌 도메인쪽으로 입출금 이벤트 요청
        publisher.publishEvent(TransferProcessed(id = 1))
    }

    // 계좌 입/출금 성공 시 진행
    @Transactional
    @EventListener(TransferSucceeded::class)
    fun successTransfer(event: TransferSucceeded) {
        logger.info("Event::Transfer Success::{}", event)
        val transferId = event.transferId

        val transfer = transferRepository.findById(transferId).orElseThrow()

        transfer.changeSuccess()

        transferRepository.save(transfer)
    }
}
