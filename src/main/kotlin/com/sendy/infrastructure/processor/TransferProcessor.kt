package com.sendy.infrastructure.processor

import com.sendy.domain.enum.TransactionHistoryTypeEnum
import com.sendy.domain.transfer.TransactionHistory
import com.sendy.domain.transfer.TransferLimit
import com.sendy.domain.transfer.TransferLimitCountProcessor
import com.sendy.infrastructure.persistence.transfer.TransferLimitJpaRepository
import org.springframework.stereotype.Component

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
        val transferLimitJpaEntity = transferLimitJpaRepository.findByUserIdAndDailyDt(userId, dailyDt)
        val withdrawTxList = transactions.filter { it.type == TransactionHistoryTypeEnum.WITHDRAW }
        val transferLimit =
            TransferLimit(
                id = transferLimitJpaEntity!!.id,
                dailyDt = transferLimitJpaEntity.dailyDt,
                dailyLimit = transferLimitJpaEntity.dailyLimit,
                singleTransactionLimit = transferLimitJpaEntity.singleTransactionLimit,
                dailyCount = transferLimitJpaEntity.dailyCount,
                createdAt = transferLimitJpaEntity.createdAt,
                updatedAt = transferLimitJpaEntity.updatedAt,
                userId = transferLimitJpaEntity.userId,
            )

        callback.invoke(withdrawTxList, transferLimit)

        transferLimitJpaEntity.dailyCount = transferLimit.dailyCount

        transferLimitJpaRepository.save(transferLimitJpaEntity)

        return transferLimit
    }
}
