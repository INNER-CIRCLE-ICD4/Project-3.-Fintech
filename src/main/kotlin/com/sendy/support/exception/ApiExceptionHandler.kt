package com.sendy.support.exception

import com.sendy.support.error.TokenErrorCode
import com.sendy.support.response.Api
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(value = 1) // 우선순위를 높게 설정
class ApiExceptionHandler {
    private val logger = LoggerFactory.getLogger(ApiExceptionHandler::class.java)

    @ExceptionHandler(value = [ApiException::class])
    fun apiException(apiException: ApiException): ResponseEntity<Api<Any>> {
        logger.error("ApiException occurred", apiException)

        val errorCode = apiException.getErrorCodeIfs()

        // Refresh Token 관련 에러는 더 명확한 메시지 제공
        val errorMessage =
            when (errorCode) {
                TokenErrorCode.EXPIRED_TOKEN -> "토큰이 만료되었습니다. 다시 로그인해주세요."
                TokenErrorCode.INVALID_TOKEN -> "유효하지 않은 토큰입니다."
                TokenErrorCode.TOKEN_EXCEPTION -> "토큰 처리 중 오류가 발생했습니다."
                TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND -> "인증 토큰이 필요합니다."
                else -> apiException.getErrorDescription()
            }

        return ResponseEntity
            .status(errorCode.httpStatusCode ?: 500)
            .body(
                Api.ERROR(errorCode, errorMessage),
            )
    }
}
