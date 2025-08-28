package com.sendy.sendyLegacyApi.infrastructure.processor.transfer

import com.sendy.sendyLegacyApi.domain.account.TransactionHistoryEntity
import com.sendy.sendyLegacyApi.domain.account.TransactionHistoryRepository
import com.sendy.sendyLegacyApi.domain.enum.TransactionHistoryTypeEnum
import com.sendy.sendyLegacyApi.domain.transfer.TransferLimitEntity
import com.sendy.sendyLegacyApi.domain.transfer.TransferLimitRepository
import com.sendy.sendyLegacyApi.infrastructure.config.TestTransferProcessorConfig
import com.sendy.sendyLegacyApi.support.exception.transfer.DailyMaxLimitException
import com.sendy.sendyLegacyApi.support.exception.transfer.SingleTxLimitException
import com.sendy.sendyLegacyApi.support.util.getTsid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestTransferProcessorConfig::class])
@Transactional
class TransferLimitCountProcessorTest(
    @Autowired
    private val transferLimitRepository: TransferLimitRepository,
    @Autowired
    private val transactionHistoryRepository: TransactionHistoryRepository,
) {
    private val transferLimitWithdrawId = getTsid()
    private val transferLimitCountProcessor = TransferLimitCountProcessor(transferLimitRepository, transactionHistoryRepository)
    private val fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault())
    private val dailyDt = LocalDateTime.now(fixed)
    private val formatDailyDt = dailyDt.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

    @Test
    @DisplayName("일일 한도 1000만원, 1회 한도 100만원에서 10만원 송금 시 정상 송금이 이뤄져야된다.")
    fun transferLimit_01() {
        // given
        transferLimitRepository.save(
            getLimitPolicy(transferLimitWithdrawId, formatDailyDt, 1_000_000, 4),
        )
        transactionHistoryRepository.saveAll(getWithdrawTransaction_success(dailyDt))

        val findAll = transactionHistoryRepository.findAll()

        // when
        transferLimitCountProcessor.processLimitCount(
            userId = 7090,
            dailyDt = dailyDt,
            amount = 100_000,
        )

        val transferLimitEntity = transferLimitRepository.findById(transferLimitWithdrawId).get()

        // then
        assertThat(transferLimitEntity.dailyCount).isEqualTo((findAll.size + 1).toLong())
    }

    @Test
    @DisplayName("일일 한도 1000만원, 1회 한도 100만원에서 100만 1원이 송금되는 경우 예외가 발생되어야된다.")
    fun transferLimit_02() {
        // given
        transferLimitRepository.save(
            getLimitPolicy(transferLimitWithdrawId, formatDailyDt, 10_000_000, 4),
        )
        transactionHistoryRepository.saveAll(getWithdrawTransaction_success(dailyDt))

        // when
        // then
        assertThrows<SingleTxLimitException> {
            transferLimitCountProcessor.processLimitCount(
                userId = 7090,
                dailyDt = dailyDt,
                amount = 1_000_001,
            )
        }
    }

    @Test
    @DisplayName("일일 한도 1000만원, 1회 한도 100만원에서 일일 한도가 99만 9_999원이면 예외가 발생되어야된다.")
    fun transferLimit_03() {
        // given
        transferLimitRepository.save(
            getLimitPolicy(transferLimitWithdrawId, formatDailyDt, 10_000_000, 1),
        )
        transactionHistoryRepository.saveAll(getWithdrawTransaction_fail(dailyDt))

        // when
        // then
        assertThrows<DailyMaxLimitException> {
            transferLimitCountProcessor.processLimitCount(
                userId = 7090,
                dailyDt = dailyDt,
                amount = 1_000_000,
            )
        }
    }

    private fun getLimitPolicy(
        id: Long,
        dailyDt: String,
        limit: Long,
        dailyCount: Long = 0,
    ) = TransferLimitEntity(
        id = id,
        dailyDt = dailyDt,
        dailyLimit = limit,
        singleTransactionLimit = 1_000_000,
        dailyCount = dailyCount,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        userId = 7090,
    )

    private fun getWithdrawTransaction_success(createdAt: LocalDateTime): List<TransactionHistoryEntity> =
        listOf(
            TransactionHistoryEntity(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.WITHDRAW,
                amount = 100_000,
                balanceAfter = 4_900_000,
                createdAt = createdAt,
                accountId = 1,
            ),
            TransactionHistoryEntity(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.WITHDRAW,
                amount = 100_000,
                balanceAfter = 4_800_000,
                createdAt = createdAt,
                transferId = null,
                accountId = 1L,
            ),
            TransactionHistoryEntity(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.WITHDRAW,
                amount = 100_000,
                balanceAfter = 4_700_000,
                createdAt = createdAt,
                accountId = 1,
            ),
            TransactionHistoryEntity(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.WITHDRAW,
                amount = 100_000,
                balanceAfter = 4_600_000,
                createdAt = createdAt,
                accountId = 1,
            ),
        )

    private fun getWithdrawTransaction_fail(createdAt: LocalDateTime): List<TransactionHistoryEntity> =
        listOf(
            TransactionHistoryEntity(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.WITHDRAW,
                amount = 9_000_001,
                balanceAfter = 10_000_000,
                createdAt = createdAt,
                accountId = 1,
            ),
        )
}
