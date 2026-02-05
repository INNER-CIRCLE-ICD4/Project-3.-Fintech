// package com.sendy.sendyLegacyApi.application.usecase.transfer
//
// import com.sendy.sendyLegacyApi.application.dto.transfer.ReserveTransferCommand
// import com.sendy.sendyLegacyApi.application.usecase.transfer.config.ReserveTransferConfig
// import com.sendy.sendyLegacyApi.domain.account.AccountEntity
// import com.sendy.sendyLegacyApi.domain.account.AccountRepository
// import com.sendy.sendyLegacyApi.domain.account.AccountStatus
// import com.sendy.sendyLegacyApi.domain.authorities.UserEntityRepository
// import com.sendy.sendyLegacyApi.domain.enum.TransferStatusEnum
// import com.sendy.sendyLegacyApi.domain.transfer.TransferRepository
// import com.sendy.sendyLegacyApi.domain.user.UserEntity
// import com.sendy.sendyLegacyApi.support.error.TransferErrorCode
// import com.sendy.sendyLegacyApi.support.exception.ServiceException
// import com.sendy.sendyLegacyApi.support.util.SHA256Util
// import org.junit.jupiter.api.Assertions.assertEquals
// import org.junit.jupiter.api.Assertions.assertThrows
// import org.junit.jupiter.api.BeforeEach
// import org.junit.jupiter.api.Test
// import org.junit.jupiter.api.assertNull
// import org.junit.jupiter.api.extension.ExtendWith
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.boot.test.context.SpringBootTest
// import org.springframework.data.repository.findByIdOrNull
// import org.springframework.test.annotation.Rollback
// import org.springframework.test.context.junit.jupiter.SpringExtension
// import org.springframework.transaction.annotation.Transactional
// import java.time.LocalDateTime
//
// @ExtendWith(SpringExtension::class)
// @SpringBootTest(
//    classes = [ReserveTransferConfig::class],
//    properties = ["spring.config.location=classpath:application.yml"],
//    webEnvironment = SpringBootTest.WebEnvironment.NONE,
// )
// @Transactional
// @Rollback
// class ReserveTransferServiceTest(
//    @Autowired
//    private val userEntityRepository: UserEntityRepository,
//    @Autowired
//    private val accountRepository: AccountRepository,
//    @Autowired
//    private val transferRepository: TransferRepository,
//    @Autowired
//    private val sha256Util: SHA256Util,
// ) {
//    private val reserveTransferService =
//        ReserveTransferService(
//            userEntityRepository,
//            accountRepository,
//            transferRepository,
//            sha256Util,
//        )
//
//    @BeforeEach
//    fun setUp() {
//        val account =
//            AccountEntity(
//                id = 1L,
//                accountNumber = "3211234123412",
//                userId = 11L,
//                password = sha256Util.hash("1234"),
//                status = AccountStatus.ACTIVE,
//                createdAt = LocalDateTime.now(),
//                updatedAt = LocalDateTime.now(),
//                balance = 1000L,
//            )
//
//        val receiverAccount =
//            AccountEntity(
//                id = 2L,
//                accountNumber = "3211234123413",
//                userId = 22L,
//                password = sha256Util.hash("1234"),
//                status = AccountStatus.ACTIVE,
//                createdAt = LocalDateTime.now(),
//                updatedAt = LocalDateTime.now(),
//                balance = 1000L,
//            )
//
//        val user =
//            UserEntity(
//                id = 1L,
//                name = "name",
//                phoneNumber = "01012341234",
//                password = sha256Util.hash("1234"),
//                email = "email@test.com",
//            )
//
//        accountRepository.save(account)
//        accountRepository.save(receiverAccount)
//        userEntityRepository.save(user)
//    }
//
//    @Test
//    fun `계좌로 예약 송금 신청하기 송금자 계좌가 유효하지 않으면 예외가 발생해야된다`() {
//        // given
//        val command =
//            ReserveTransferCommand(
//                sendUserId = 3L,
//                receivePhoneNumber = "01012341234",
//                scheduledAt = LocalDateTime.now(),
//                requestedAt = LocalDateTime.now(),
//                password = "1234",
//                amount = 1_000L,
//                sendAccountNumber = "3211234123412",
//            )
//
//        // when
//        val assertThrows =
//            assertThrows(ServiceException::class.java) {
//                reserveTransferService.reserveTransfer(command)
//            }
//
//        // then
//        assertEquals(assertThrows.message, TransferErrorCode.NOT_FOUND_SENDER_ACCOUNT.description)
//    }
//
//    @Test
//    fun `휴대 전화번호로 예약 송금 신청이 된다면 상태는 RESERVED 이고 수취자 계좌번호는 null 이어야된다`() {
//        // given
//        val command =
//            ReserveTransferCommand(
//                sendUserId = 1L,
//                receivePhoneNumber = "01012341234",
//                scheduledAt = LocalDateTime.now(),
//                requestedAt = LocalDateTime.now(),
//                password = "1234",
//                amount = 1_000L,
//                sendAccountNumber = "3211234123412",
//            )
//
//        // when
//        val reserveTransfer = reserveTransferService.reserveTransfer(command)
//        val transfer = transferRepository.findByIdOrNull(reserveTransfer.transferId)
//
//        // then
//        assertEquals(transfer?.status, TransferStatusEnum.RESERVE)
//        assertNull(transfer?.receiveAccountNumber)
//    }
//
//    @Test
//    fun `본인 계좌로 예약 송금이 신청 된다면 예외가 발생해야된다`() {
//        // given
//        val command =
//            ReserveTransferCommand(
//                sendUserId = 1L,
//                receiveAccountNumber = "3211234123412",
//                scheduledAt = LocalDateTime.now(),
//                requestedAt = LocalDateTime.now(),
//                password = "1234",
//                amount = 1_000L,
//                sendAccountNumber = "3211234123412",
//            )
//
//        // when
//        val assertThrows =
//            assertThrows(ServiceException::class.java) {
//                reserveTransferService.reserveTransfer(command)
//            }
//
//        // then
//        assertEquals(assertThrows.message, TransferErrorCode.INVALID_SELF_ACCOUNT.description)
//    }
//
//    @Test
//    fun `계좌로 예약 송금이 상대 계좌가 유효하지 않다면 예외가 발생해야된다`() {
//        // given
//        val command =
//            ReserveTransferCommand(
//                sendUserId = 1L,
//                receiveAccountNumber = "3211234123414",
//                scheduledAt = LocalDateTime.now(),
//                requestedAt = LocalDateTime.now(),
//                password = "1234",
//                amount = 1_000L,
//                sendAccountNumber = "3211234123412",
//            )
//
//        // when
//        val assertThrows =
//            assertThrows(ServiceException::class.java) {
//                reserveTransferService.reserveTransfer(command)
//            }
//
//        // then
//        assertEquals(assertThrows.message, TransferErrorCode.NOT_FOUND_RECEIVER_ACCOUNT.description)
//    }
//
//    @Test
//    fun `계좌로 예약 송금이 신청된다면 상태는 RESERVE 이고 수취인 휴대폰 번호는 null이어야된다`() {
//        // given
//        val command =
//            ReserveTransferCommand(
//                sendUserId = 1L,
//                receiveAccountNumber = "3211234123413",
//                scheduledAt = LocalDateTime.now(),
//                requestedAt = LocalDateTime.now(),
//                password = "1234",
//                amount = 1_000L,
//                sendAccountNumber = "3211234123412",
//            )
//
//        // when
//        val reserveTransfer = reserveTransferService.reserveTransfer(command)
//        val transfer = transferRepository.findByIdOrNull(reserveTransfer.transferId)
//
//        // then
//        assertEquals(transfer?.status, TransferStatusEnum.RESERVE)
//        assertNull(transfer?.receivePhoneNumber)
//    }
// }
