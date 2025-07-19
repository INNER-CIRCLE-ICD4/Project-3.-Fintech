package com.common.domain.exceptions

import com.common.domain.Api.Api
import com.common.domain.error.ErrorCode
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(value = Int.MAX_VALUE) // 가장 마지막에 실행 적용
class GlobalExceptionHandler {
    
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(value = [Exception::class])
    fun exception(exception: Exception): ResponseEntity<Api<Any>> {
        logger.error("", exception)

        return ResponseEntity
            .status(500)
            .body(Api.ERROR(ErrorCode.SERVER_ERROR))
    }
}
