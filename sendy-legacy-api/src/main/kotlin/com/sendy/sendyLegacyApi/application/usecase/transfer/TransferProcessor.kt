package com.sendy.sendyLegacyApi.application.usecase.transfer

import com.sendy.sendyLegacyApi.application.dto.transfer.TransferMoneyCommand
import com.sendy.sendyLegacyApi.domain.account.AccountEntity
import com.sendy.sendyLegacyApi.domain.account.AccountRepository
import com.sendy.sendyLegacyApi.domain.account.TransactionHistoryEntity
import com.sendy.sendyLegacyApi.domain.account.TransactionHistoryRepository
import com.sendy.sendyLegacyApi.domain.auth.UserEntityRepository
import com.sendy.sendyLegacyApi.domain.enum.TransactionHistoryTypeEnum
import com.sendy.sendyLegacyApi.domain.transfer.TransferEntity
import com.sendy.sendyLegacyApi.domain.transfer.TransferLimitCountProcessor
import com.sendy.sendyLegacyApi.support.error.TransferErrorCode
import com.sendy.sendyLegacyApi.support.exception.ServiceException
import com.sendy.sendyLegacyApi.support.util.SHA256Util
import com.sendy.sendyLegacyApi.support.util.getTsid
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class TransferProcessor(
    private val accountRepository: AccountRepository,
    private val transferLimitCountProcessor: TransferLimitCountProcessor,
    private val userEntityRepository: UserEntityRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val shA256Util: SHA256Util,
) {
    @Transactional
    fun doTransferWithPassword(command: TransferMoneyCommand) {
        // 출금 계좌 조회 시 lock 획득 후 진행
        val senderAccount =
            accountRepository.findOneBySenderUserId(command.sendUserId)
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
                .findByPhoneNumberAndDeleteAtIsNull(command.receivePhoneNumber)
                ?.also {
                    if (it.name != command.receiveName) {
                        throw ServiceException(TransferErrorCode.INVALID_RECEIVER_NAME)
                    }
                } ?: throw ServiceException(TransferErrorCode.INVALID_RECEIVER_PHONE_NUMBER)

        // 수취인 계좌 유효한지 체크 -> 예외 발생 시 롤백
        val receiveAccount =
            accountRepository.findOneByReceiverUserId(receiver.id)
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
    }

    @Transactional
    fun doTransferNoPassword(transfer: TransferEntity) {
        // 출금 계좌 조회 시 lock 획득 후 진행
        val senderAccount =
            accountRepository.findOneBySenderUserId(transfer.sendUserId)
                ?: throw EntityNotFoundException("송금자의 계좌를 찾을 수 없습니다.")

        // 출금 계좌 유효한지 체크 -> 예외 발생 이후 진행X
        senderAccount.checkActiveAndInvokeError()

        // 출금 계좌 잔액 체크 -> 예외 발생 이후 진행X
        senderAccount.checkRemainAmountAndInvokeError(transfer.amount)

        // 일일 이체 한도 체크
        transferLimitCountProcessor.processLimitCount(transfer.sendUserId, LocalDateTime.now(), transfer.amount)

        // 계좌 출금
        senderAccount.withdraw(transfer.amount)

        // 수취인 휴대폰 기반으로 계좌 조회
        val receiveAccount: AccountEntity? =
            when (true) {
                (transfer.receivePhoneNumber != null) -> {
                    val userEntity =
                        userEntityRepository
                            .findByPhoneNumberAndDeleteAtIsNull(transfer.receivePhoneNumber!!)
                            ?: throw ServiceException(TransferErrorCode.INVALID_RECEIVER_PHONE_NUMBER)

                    accountRepository.findOneByReceiverUserId(userEntity.id)
                        ?: throw ServiceException(TransferErrorCode.NOT_FOUND_RECEIVER_ACCOUNT)
                }

                (transfer.receiveAccountNumber != null) -> {
                    accountRepository.findOneByAccountNumber(transfer.receiveAccountNumber!!)
                        ?: throw ServiceException(TransferErrorCode.NOT_FOUND_RECEIVER_ACCOUNT)
                }

                else -> null
            }

        // 수취인 계좌로 입금 -> 예외 발생 시 롤백
        receiveAccount?.checkActiveAndInvokeError()

        // 계좌 입금
        receiveAccount?.deposit(transfer.amount)

        // 계좌 내역 저장(출금 내역)
        transactionHistoryRepository.save(
            TransactionHistoryEntity(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.WITHDRAW,
                amount = transfer.amount,
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
                amount = transfer.amount,
                balanceAfter = receiveAccount?.balance!!,
                description = "계좌 입금",
                createdAt = LocalDateTime.now(),
                accountId = receiveAccount.id,
            ),
        )
    }
}
