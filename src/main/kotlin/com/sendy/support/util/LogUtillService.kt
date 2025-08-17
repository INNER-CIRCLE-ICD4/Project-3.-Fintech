package com.sendy.support.util

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerMapping


@Component
class LogUtillService {
    private val log = LoggerFactory.getLogger(LogUtillService::class.java)
    fun logMdc(
        schedulerStep: String,
        entityId: Long?
    ) {
        try {
            MDC.put("schedulerStep", schedulerStep)
            MDC.put("entityId", entityId.toString())

        } finally {
            MDC.clear()
        }
    }



}