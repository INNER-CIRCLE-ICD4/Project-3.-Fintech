package com.sendy.infrastructure.processor.transfer

import com.sendy.domain.account.TransactionHistoryRepository
import com.sendy.domain.transfer.TransferLimitCountProcessor
import com.sendy.domain.transfer.TransferLimitEntity
import com.sendy.domain.transfer.TransferLimitRepository
import com.sendy.support.exception.transfer.DailyMaxLimitException
import com.sendy.support.exception.transfer.PastNotTransferException
import com.sendy.support.exception.transfer.SingleTxLimitException
import com.sendy.support.util.getTsid
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Component
class TransferProcessor(
    private val transferLimitRepository: TransferLimitRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
) : TransferLimitCountProcessor {
    override fun processLimitCount(
        userId: Long,
        dailyDt: LocalDateTime,
        amount: Long,
    ) {
        // dailyDt가 현재 날짜가 아니면 예외 발생
        if (LocalDate
                .now()
                .isEqual(
                    dailyDt.toLocalDate(),
                ).not()
        ) {
            throw PastNotTransferException()
        }

        val dailyDtString = dailyDt.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        val findEntity = transferLimitRepository.findByUserIdAndDailyDt(userId, dailyDtString)

        if (findEntity == null) {
            transferLimitRepository.save(
                TransferLimitEntity(
                    id = getTsid(),
                    dailyDt = dailyDtString,
                    dailyLimit = 10_000_000,
                    singleTransactionLimit = 1_000_000,
                    dailyCount = 1,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                    userId = userId,
                ),
            )
        } else {
            // 일일 출금 내역 조회(최대 10개)
            val startDt = dailyDt.toLocalDate().atTime(LocalTime.MIN)
            val endDt = dailyDt.toLocalDate().atTime(LocalTime.MAX) // 23:59:59.999999999

            val findByDailyDt = transactionHistoryRepository.findWithDrawToday(startDt, endDt)

            // 출금 내역 amount 합산
            val totalAmount = findByDailyDt.map { it.amount }.reduce { prev, next -> prev + next }

            // 현재 보내려는 송금액, 일일 최대 한도 금액, 1회 요청 최대 한도 금액 모두 체크
            when {
                amount > findEntity.singleTransactionLimit ->
                    throw SingleTxLimitException(findEntity.singleTransactionLimit)
                totalAmount + amount > findEntity.dailyLimit ->
                    throw DailyMaxLimitException(amount, findEntity.dailyLimit - totalAmount)
            }

            findEntity.dailyCount++
            transferLimitRepository.save(findEntity)
        }
    }
}
