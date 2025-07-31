package com.sendy.infrastructure.persistence.transfer

import com.sendy.domain.transfer.TransferLimitRepository
import org.springframework.stereotype.Repository

@Repository
class TransferLimitInMemoryRepository(
    private val transferLimitJpaRepository: TransferLimitJpaRepository,
) : TransferLimitRepository {
    override fun getTransferLimitBy(
        userId: Long,
        dailyDt: String,
    ): TransferLimit? =
        transferLimitJpaRepository.findByUserIdAndDailyDt(userId, dailyDt)?.let {
            TransferLimit(
                id = it.id,
                dailyDt = it.dailyDt,
                dailyLimit = it.dailyLimit,
                dailyCount = it.dailyCount,
                singleTransactionLimit = it.singleTransactionLimit,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                userId = it.userId,
            )
        }

    override fun save(domain: TransferLimit): TransferLimit {
        val existing: TransferLimitJpaEntity = transferLimitJpaRepository.findById(domain.id).orElse(null)

        existing.dailyCount = domain.dailyCount

        val save =
            transferLimitJpaRepository.save(existing)

        return TransferLimit(
            id = save.id,
            dailyDt = save.dailyDt,
            dailyLimit = save.dailyLimit,
            dailyCount = save.dailyCount,
            singleTransactionLimit = save.singleTransactionLimit,
            createdAt = save.createdAt,
            updatedAt = save.updatedAt,
            userId = save.userId,
        )
    }
}
