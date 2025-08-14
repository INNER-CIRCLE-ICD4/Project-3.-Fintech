package com.sendy.transferScheduler.application.service

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduledReserveTransferService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelayString = "1s")
    fun scheduled() {
        logger.info("scheduled..")
    }
}
