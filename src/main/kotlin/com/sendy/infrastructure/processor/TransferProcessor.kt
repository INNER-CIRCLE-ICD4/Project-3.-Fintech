package com.sendy.infrastructure.processor

import com.sendy.domain.enum.TransactionHistoryTypeEnum
import com.sendy.domain.transfer.TransactionHistory
import com.sendy.domain.transfer.TransferLimit
import com.sendy.domain.transfer.TransferLimitCountProcessor
import com.sendy.infrastructure.persistence.transfer.TransferLimitJpaEntity
import com.sendy.infrastructure.persistence.transfer.TransferLimitJpaRepository
import com.sendy.support.util.getTsid
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TransferProcessor(
    private val transferLimitJpaRepository: TransferLimitJpaRepository,
) : TransferLimitCountProcessor {
    override fun processLimitCount(
        transactions: List<TransactionHistory>,
        userId: Long,
        dailyDt: String,
        callback: (withdrawTxList: List<TransactionHistory>, transferLimit: TransferLimit) -> Unit,
    ): TransferLimit {
        val findEntity = transferLimitJpaRepository.findByUserIdAndDailyDt(userId, dailyDt)

        val transferLimit =
            if (findEntity == null) {
                TransferLimit(
                    id = getTsid(),
                    dailyDt = dailyDt,
                    dailyLimit = 10_000_000,
                    singleTransactionLimit = 1_000_000,
                    dailyCount = 0,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                    userId = userId,
                )
            } else {
                TransferLimit(
                    id = findEntity.id,
                    dailyDt = findEntity.dailyDt,
                    dailyLimit = findEntity.dailyLimit,
                    singleTransactionLimit = findEntity.singleTransactionLimit,
                    dailyCount = findEntity.dailyCount,
                    createdAt = findEntity.createdAt,
                    updatedAt = findEntity.updatedAt,
                    userId = findEntity.userId,
                )
            }

        val withdrawTxList = transactions.filter { it.type == TransactionHistoryTypeEnum.WITHDRAW }

        callback.invoke(withdrawTxList, transferLimit)

        if (findEntity == null) {
            transferLimitJpaRepository.save(
                TransferLimitJpaEntity(
                    id = transferLimit.id,
                    dailyDt = transferLimit.dailyDt,
                    dailyLimit = transferLimit.dailyLimit,
                    singleTransactionLimit = transferLimit.singleTransactionLimit,
                    dailyCount = transferLimit.dailyCount,
                    createdAt = transferLimit.createdAt,
                    updatedAt = transferLimit.updatedAt,
                    userId = transferLimit.userId,
                ),
            )
        } else {
            findEntity.dailyCount = transferLimit.dailyCount

            transferLimitJpaRepository.save(findEntity)
        }

        return transferLimit
    }
}
