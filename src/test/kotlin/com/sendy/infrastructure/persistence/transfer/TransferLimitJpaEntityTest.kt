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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = ["aes256.key=d2f3de5ce2bef6aad4c2b01e2bffdcd6"])
class TransferLimitJpaEntityTest(
    @Autowired
    private val transferLimitJpaRepository: TransferLimitJpaRepository,
    @Autowired
    private val transferLimitCountProcessor: TransferLimitCountProcessor,
) {
    private val transferLimitId = getTsid()

    @BeforeEach
    fun setUp() {
        transferLimitJpaRepository.save(
            getLimitPolicy(transferLimitId, "20250721", 1_000_000),
        )
    }

    @Test
    @DisplayName("일일 이체 한도(100만원)내에서 송금 할 수 있어야된다.")
    fun transferLimit_01() {
        // given
        val dailyDt = "20250721"
        val userId = 7090L
        val amount = 900_000
        val transactions = getTransaction(dailyDt)

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
    ) = TransferLimitJpaEntity(
        id = id,
        dailyDt = dailyDt,
        dailyLimit = limit,
        singleTransactionLimit = 100_000,
        dailyCount = 0,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        userId = 7090,
    )

    private fun getTransaction(dailyDt: String): List<TransactionHistory> {
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
}
