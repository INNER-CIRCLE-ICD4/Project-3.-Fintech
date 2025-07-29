package com.sendy.application.usecase.transfer

import com.sendy.application.dto.transfer.TransferMoneyCommand
import com.sendy.application.usecase.transfer.command.TransferMoneyUseCase
import com.sendy.domain.account.AccountEntity
import com.sendy.domain.account.AccountStatus
import com.sendy.domain.enum.TransferStatusEnum
import com.sendy.domain.transfer.TransferEntity
import com.sendy.domain.transfer.TransferRepository
import com.sendy.infrastructure.persistence.account.AccountRepository
import com.sendy.infrastructure.persistence.account.TransactionHistoryRepository
import com.sendy.support.util.getTsid
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime

@Service
class TransferService(
    private val transferRepository: TransferRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val accountRepository: AccountRepository,
    private val platformTransactionManager: PlatformTransactionManager,
) : TransferMoneyUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun transferMoney(command: TransferMoneyCommand) {
        // 이체 이력 PENDING 먼저 저장
        saveTransferPending(command.amount, command.requestedAt)

        // 계좌 비밀번호 인증 체크

        // 송금 계좌 조회 시 lock 획득 후 진행
        val senderAccount =
            accountRepository.findOneByAccountNumberForUpdate(command.senderAccountNumber)
                ?: throw EntityNotFoundException()
        // 송금 계좌 유효한지 체크 -> 예외 발생 이후 진행X
        if (senderAccount.status != AccountStatus.ACTIVE) {
            throw RuntimeException("예외 발생")
        }

        // 송금 계좌 잔액 체크 -> 예외 발생 이후 진행X
        if (senderAccount.balance < command.amount) {
            throw RuntimeException("예외 발생")
        }

        // start transaction
        // 송금 계좌 출금

        // 수취인 계좌 유효한지 체크 -> 예외 발생 시 롤백
        // 수취인 계좌로 입금 -> 예외 발생 시 롤백
        // end transaction

        // 송금 완료 상태 변경
    }

    private fun saveTransferPending(
        amount: Long,
        requestedAt: LocalDateTime,
    ) {
        val transferId = getTsid()
        transferRepository.save(
            TransferEntity(
                id = transferId,
                amount = amount,
                status = TransferStatusEnum.PENDING,
                requestedAt = requestedAt,
            ),
        )
    }

    private fun transfer(
        amount: Long,
        senderAccount: AccountEntity,
        receiverAccountNumber: String,
    ) {
        TransactionTemplate(platformTransactionManager).execute {
        }
    }
}
