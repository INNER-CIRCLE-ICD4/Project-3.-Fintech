package com.sendy.infrastructure.persistence.transfer

import com.sendy.domain.enum.TransactionHistoryTypeEnum
import com.sendy.domain.transfer.TransactionHistory
import com.sendy.domain.transfer.TransferLimitCountProcessor
import com.sendy.support.util.getTsid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import kotlin.test.Ignore

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = ["aes256.key=d2f3de5ce2bef6aad4c2b01e2bffdcd6"])
class TransferLimitJpaEntityTest(
    @Autowired
    private val transferLimitJpaRepository: TransferLimitJpaRepository,
    @Autowired
    private val transferLimitCountProcessor: TransferLimitCountProcessor,
) {
    private val transferLimitDepositId = getTsid()
    private val transferLimitWithdrawId = getTsid()

    @BeforeEach
    fun setUp() {
        transferLimitJpaRepository.save(
            getLimitPolicy(transferLimitDepositId, "20250721", 1_000_000),
        )

        transferLimitJpaRepository.save(
            getLimitPolicy(transferLimitWithdrawId, "20250722", 1_000_000, 4),
        )
    }

    @Test
    @DisplayName("일일 이체 한도(100만원)내에서 송금 할 수 있어야된다.")
    fun transferLimit_01() {
        // given
        val dailyDt = "20250721"
        val userId = 7090L
        val amount = 900_000
        val transactions = getDepositTransaction(dailyDt)

        // when
        val domain =
            transferLimitCountProcessor.processLimitCount(transactions, userId, dailyDt) { tx, transferLimit ->
                transferLimit.incrementDailyCount(tx, amount)
            }

        // then
        assertThat(domain.dailyCount).isEqualTo(1)
    }

    @Test
    @DisplayName("계좌에 500만원이 있고, 10만원씩 4번 출금 시 일일 이체 한도(100만원)내에서 최대 10만원 송금 할 수 있어야된다.")
    @Ignore
    fun transferLimit_02() {
        // given
        val dailyDt = "20250722"
        val userId = 7090L
        val amount = 100_000
        val transactions = getDepositTransaction(dailyDt)

        // when
        val domain =
            transferLimitCountProcessor.processLimitCount(transactions, userId, dailyDt) { tx, transferLimit ->
                transferLimit.incrementDailyCount(tx, amount)
            }

        // then
        assertThat(domain.dailyCount).isEqualTo(1)
    }

    private fun getLimitPolicy(
        id: Long,
        dailyDt: String,
        limit: Long,
        dailyCount: Long = 0,
    ) = TransferLimitJpaEntity(
        id = id,
        dailyDt = dailyDt,
        dailyLimit = limit,
        singleTransactionLimit = 100_000,
        dailyCount = dailyCount,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        userId = 7090,
    )

    private fun getDepositTransaction(dailyDt: String): List<TransactionHistory> {
        val yyyy = dailyDt.slice(0..3)
        val mm = dailyDt.slice(4..5)
        val dd = dailyDt.slice(6..7)

        val createdAt = LocalDateTime.of(yyyy.toInt(), mm.toInt(), dd.toInt(), 0, 0, 0)
        return listOf(
            TransactionHistory(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.DEPOSIT,
                amount = 10_000,
                balanceAfter = 10_000,
                createdAt = createdAt,
                transferId = 1,
            ),
            TransactionHistory(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.DEPOSIT,
                amount = 10_000,
                balanceAfter = 20_000,
                createdAt = createdAt,
                transferId = 1,
            ),
            TransactionHistory(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.DEPOSIT,
                amount = 100_000,
                balanceAfter = 120_000,
                createdAt = createdAt,
                transferId = 1,
            ),
            TransactionHistory(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.DEPOSIT,
                amount = 780_000,
                balanceAfter = 900_000,
                createdAt = createdAt,
                transferId = 1,
            ),
        )
    }

    private fun getWithdrawTransaction(dailyDt: String): List<TransactionHistory> {
        val yyyy = dailyDt.slice(0..3)
        val mm = dailyDt.slice(4..5)
        val dd = dailyDt.slice(6..7)

        val createdAt = LocalDateTime.of(yyyy.toInt(), mm.toInt(), dd.toInt(), 0, 0, 0)
        return listOf(
            TransactionHistory(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.WITHDRAW,
                amount = 100_000,
                balanceAfter = 4_900_000,
                createdAt = createdAt,
                transferId = 1,
            ),
            TransactionHistory(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.WITHDRAW,
                amount = 100_000,
                balanceAfter = 4_800_000,
                createdAt = createdAt,
                transferId = 1,
            ),
            TransactionHistory(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.WITHDRAW,
                amount = 100_000,
                balanceAfter = 4_700_000,
                createdAt = createdAt,
                transferId = 1,
            ),
            TransactionHistory(
                id = getTsid(),
                type = TransactionHistoryTypeEnum.WITHDRAW,
                amount = 100_000,
                balanceAfter = 4_600_000,
                createdAt = createdAt,
                transferId = 1,
            ),
        )
    }
}
