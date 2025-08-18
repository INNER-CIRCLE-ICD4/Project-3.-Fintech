package com.sendy.sendyLegacyApi.application.usecase.transfer

import com.sendy.sendyLegacyApi.application.dto.transfer.ReserveTransferCommand
import com.sendy.sendyLegacyApi.application.usecase.transfer.config.ReserveTransferConfig
import com.sendy.sendyLegacyApi.domain.account.AccountRepository
import com.sendy.sendyLegacyApi.domain.auth.UserEntityRepository
import com.sendy.sendyLegacyApi.domain.transfer.TransferRepository
import com.sendy.sendyLegacyApi.support.error.TransferErrorCode
import com.sendy.sendyLegacyApi.support.exception.ServiceException
import com.sendy.sendyLegacyApi.support.util.SHA256Util
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [ReserveTransferConfig::class])
class ReserveTransferServiceTest(
    @Autowired
    private val userEntityRepository: UserEntityRepository,
    @Autowired
    private val accountRepository: AccountRepository,
    @Autowired
    private val transferRepository: TransferRepository,
    @Autowired
    private val sha256Util: SHA256Util,
) {
    private val reserveTransferService =
        ReserveTransferService(
            userEntityRepository,
            accountRepository,
            transferRepository,
            sha256Util,
        )

    @Test
    fun `계좌로 예약 송금 신청하기 송금자 계좌가 유효하지 않으면 예외가 발생해야된다`() {
        // given
        val command =
            ReserveTransferCommand(
                sendUserId = 1L,
                receivePhoneNumber = "01012341234",
                scheduledAt = LocalDateTime.now(),
                requestedAt = LocalDateTime.now(),
                password = "1234",
                amount = 1_000L,
                sendAccountNumber = "3211234123412",
            )

        // when
        val assertThrows =
            assertThrows(ServiceException::class.java) {
                reserveTransferService.reserveTransfer(command)
            }

        // then
        assertEquals(assertThrows.message, TransferErrorCode.NOT_FOUND_SENDER_ACCOUNT.description)
    }
}
