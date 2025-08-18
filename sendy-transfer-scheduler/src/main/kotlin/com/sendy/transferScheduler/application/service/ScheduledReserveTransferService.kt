package com.sendy.transferScheduler.application.service

import com.sendy.transferDomain.domain.Transfer
import com.sendy.transferDomain.domain.TransferRepository
import com.sendy.transferDomain.domain.enum.TransferStatusEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class ScheduledReserveTransferService(
    private val transferRepository: TransferRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelayString = "10s")
    fun scheduled() {
//        val dateNow = LocalDate.now()
//        val start = LocalDateTime.of(dateNow, LocalTime.of(0, 0, 0))
//        val end = LocalDateTime.of(dateNow, LocalTime.of(23, 59, 59))
//        val findReservedTransfer = transferRepository.findReservedTransfer(start, end)
        logger.info("scheduled.. start")

        val dummyTransfer = dummyTransfer()

        val mutableList = mutableListOf<Transfer>()
        dummyTransfer.forEachIndexed { index, transfer ->
            val i = index + 1
            mutableList.add(transfer)

            if (i % 1_000 == 0) {
                CoroutineScope(Dispatchers.IO).runCatching {
                    launch {
                        transferCoroutine(mutableList)
                    }
                }

                mutableList.removeAll { it.id > 0 }
            }
        }

        logger.info("scheduled.. end")
    }

    private fun transferCoroutine(list: List<Transfer>) {
        println("transfer start")
        Thread.sleep(3_000)
        println("transfer end")
    }

    private fun dummyTransfer(): List<Transfer> {
        val max = 500_000
        val map =
            (0..<max).map {
                Transfer(
                    id = it.toLong(),
                    amount = it.toLong(),
                    scheduledAt = LocalDateTime.now(),
                    status = TransferStatusEnum.PENDING,
                    requestedAt = LocalDateTime.now(),
                    sendAccountNumber = "1",
                    sendUserId = it.toLong(),
                    completedAt = null,
                    reason = null,
                    receiveAccountNumber = null,
                    receivePhoneNumber = null,
                )
            }

        Thread.sleep(Duration.of(3L, ChronoUnit.SECONDS))

        return map
    }
}
