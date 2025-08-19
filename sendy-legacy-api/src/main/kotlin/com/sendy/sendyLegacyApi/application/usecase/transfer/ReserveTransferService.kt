package com.sendy.sendyLegacyApi.application.usecase.transfer

import com.sendy.sendyLegacyApi.application.dto.transfer.ReserveTransferCommand
import com.sendy.sendyLegacyApi.application.dto.transfer.TransferId
import com.sendy.sendyLegacyApi.application.usecase.transfer.command.ReserveTransferUseCase
import com.sendy.sendyLegacyApi.domain.account.AccountRepository
import com.sendy.sendyLegacyApi.domain.auth.UserEntityRepository
import com.sendy.sendyLegacyApi.domain.enum.TransferStatusEnum
import com.sendy.sendyLegacyApi.domain.transfer.TransferEntity
import com.sendy.sendyLegacyApi.domain.transfer.TransferRepository
import com.sendy.sendyLegacyApi.support.error.TransferErrorCode
import com.sendy.sendyLegacyApi.support.exception.ServiceException
import com.sendy.sendyLegacyApi.support.util.SHA256Util
import com.sendy.sendyLegacyApi.support.util.getTsid
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReserveTransferService(
    private val userEntityRepository: UserEntityRepository,
    private val accountRepository: AccountRepository,
    private val transferRepository: TransferRepository,
    private val sha256Util: SHA256Util,
) : ReserveTransferUseCase {
    @Transactional
    override fun reserveTransfer(command: ReserveTransferCommand): TransferId {
        // 보내는이에 계좌가 없으면 -> 예외 발생
        val senderAccount =
            accountRepository.findByIdOrNull(command.sendUserId) ?: throw ServiceException(
                TransferErrorCode.NOT_FOUND_SENDER_ACCOUNT,
            )

        // 보내는이 계좌 유효한지 체크 -> 예외 발생
        senderAccount.checkActiveAndInvokeError()

        // 비밀번호 틀린 경우 -> 예외 발생
        sha256Util.matches(command.password, senderAccount.password)

        val transferEntity =
            TransferEntity(
                id = getTsid(),
                sendUserId = command.sendUserId,
                sendAccountNumber = command.sendAccountNumber,
                amount = command.amount,
                status = TransferStatusEnum.RESERVED,
                scheduledAt = command.scheduledAt,
                requestedAt = command.requestedAt,
            )

        // 계좌로 송금 하는 경우
        // 본인 계좌로 예약 송금 -> 예외 발생
        command.receiveAccountNumber?.let {
            senderAccount.checkSelfAndInvokeError(it)
            // 상대방 계좌 유효 하지않으면 -> 예외 발생
            accountRepository.findByAccountNumber(it)
                ?: throw ServiceException(TransferErrorCode.NOT_FOUND_RECEIVER_ACCOUNT)

            transferEntity.receiveAccountNumber = it
        }

        // 휴대폰 번호로 송금 하는 경우
        command.receivePhoneNumber?.let {
            val receiveUser =
                userEntityRepository.findByPhoneNumber(it)

            receiveUser?.checkSelfAndInvokeError(it)

            transferEntity.receivePhoneNumber = it
        }

        transferRepository.save(transferEntity)

        return TransferId(transferEntity.id)
    }
}
