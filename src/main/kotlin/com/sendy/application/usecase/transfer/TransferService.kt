package com.sendy.application.usecase.transfer

import com.sendy.application.dto.transfer.TransferMoneyCommand
import com.sendy.application.dto.transfer.TransferMoneyResponse
import com.sendy.application.usecase.transfer.command.TransferMoneyUseCase
import com.sendy.domain.account.AccountRepository
import com.sendy.domain.account.TransactionHistoryEntity
import com.sendy.domain.account.TransactionHistoryRepository
import com.sendy.domain.auth.UserEntityRepository
import com.sendy.domain.enum.TransactionHistoryTypeEnum
import com.sendy.domain.enum.TransferStatusEnum
import com.sendy.domain.transfer.TransferEntity
import com.sendy.domain.transfer.TransferLimitCountProcessor
import com.sendy.domain.transfer.TransferRepository
import com.sendy.support.error.TransferErrorCode
import com.sendy.support.exception.ServiceException
import com.sendy.support.util.SHA256Util
import com.sendy.support.util.getTsid
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime

@Service
class TransferService(
    private val userEntityRepository: UserEntityRepository,
    private val transferRepository: TransferRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val accountRepository: AccountRepository,
    private val platformTransactionManager: PlatformTransactionManager,
    private val transferLimitCountProcessor: TransferLimitCountProcessor,
    private val shA256Util: SHA256Util,
) : TransferMoneyUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun transferMoney(command: TransferMoneyCommand): TransferMoneyResponse {
        // 이체 이력 PENDING 먼저 저장
        val transfer =
            transferRepository.save(
                TransferEntity(
                    id = getTsid(),
                    amount = command.amount,
                    status = TransferStatusEnum.PENDING,
                    requestedAt = command.requestedAt,
                    sendUserId = command.sendUserId,
                    sendAccountNumber = command.sendAccountNumber,
                    receivePhoneNumber = command.receivePhoneNumber,
                ),
            )

        try {
            logger.debug("start transfer transaction")
            TransactionTemplate(platformTransactionManager).execute {
                // 출금 계좌 조회 시 lock 획득 후 진행
                val senderAccount =
                    accountRepository.findOneBySenderAccountNumber(command.sendAccountNumber)
                        ?: throw EntityNotFoundException("송금자의 계좌를 찾을 수 없습니다.")

                // 출금 계좌 유효한지 체크 -> 예외 발생 이후 진행X
                senderAccount.checkActiveAndInvokeError()

                // 출금 계좌 잔액 체크 -> 예외 발생 이후 진행X
                senderAccount.checkRemainAmountAndInvokeError(command.amount)

                // 일일 이체 한도 체크
                transferLimitCountProcessor.processLimitCount(command.sendUserId, LocalDateTime.now(), command.amount)

                // 계좌 비밀번호 인증 체크
                val matches = shA256Util.matches(command.password, senderAccount.password)
                if (matches.not()) {
                    throw ServiceException(TransferErrorCode.INVALID_ACCOUNT_NUMBER_PASSWORD)
                }

                // 계좌 출금
                senderAccount.withdraw(command.amount)

                // 수취인 휴대폰 기반으로 계좌 조회

                val receiver =
                    userEntityRepository
                        .findByPhoneNumberAndIsDeleteFalse(command.receivePhoneNumber)
                        .orElseThrow { throw ServiceException(TransferErrorCode.INVALID_RECEIVER_PHONE_NUMBER) }

                // 수취인 계좌 유효한지 체크 -> 예외 발생 시 롤백
                val receiveAccount =
                    accountRepository.findOneByReceiveAccountNumber(command.receiveAccountNumber)
                        ?: throw ServiceException(TransferErrorCode.NOT_FOUND_RECEIVER_ACCOUNT)

                // 수취인 계좌로 입금 -> 예외 발생 시 롤백
                receiveAccount.checkActiveAndInvokeError()

                // 계좌 입금
                receiveAccount.deposit(command.amount)

                // 계좌 내역 저장(출금 내역)
                transactionHistoryRepository.save(
                    TransactionHistoryEntity(
                        id = getTsid(),
                        type = TransactionHistoryTypeEnum.WITHDRAW,
                        amount = command.amount,
                        balanceAfter = senderAccount.balance,
                        description = "계좌 출금",
                        createdAt = LocalDateTime.now(),
                        accountId = senderAccount.id,
                    ),
                )

                // 계좌 내역 저장(입금 내역)
                transactionHistoryRepository.save(
                    TransactionHistoryEntity(
                        id = getTsid(),
                        type = TransactionHistoryTypeEnum.DEPOSIT,
                        amount = command.amount,
                        balanceAfter = receiveAccount.balance,
                        description = "계좌 입금",
                        createdAt = LocalDateTime.now(),
                        accountId = receiveAccount.id,
                    ),
                )

                logger.debug("end transfer transaction")
            }

            // 송금 완료 상태 변경
            transfer.changeToSuccess()
            transferRepository.save(transfer)
            return TransferMoneyResponse(transfer.status)
        } catch (e: ServiceException) {
            transfer.changeToFail()
            transferRepository.save(transfer)
            logger.debug("rollback transaction")

            throw e
        }
    }
}
