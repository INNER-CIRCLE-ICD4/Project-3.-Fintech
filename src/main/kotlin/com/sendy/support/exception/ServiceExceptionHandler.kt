package com.sendy.support.exception

import com.sendy.support.error.TokenErrorCode
import com.sendy.support.response.Response
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ServiceExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(value = [ServiceException::class])
    fun serviceHandler(serviceException: ServiceException): Response<Any> {
        logger.error("ServiceException Occurred", serviceException)

        val errorCode = serviceException.getErrorCodeIfs()

        // Refresh Token 관련 에러는 더 명확한 메시지 제공
        val errorMessage =
            when (errorCode) {
                TokenErrorCode.EXPIRED_TOKEN -> "토큰이 만료되었습니다. 다시 로그인해주세요."
                TokenErrorCode.INVALID_TOKEN -> "유효하지 않은 토큰입니다."
                TokenErrorCode.TOKEN_EXCEPTION -> "토큰 처리 중 오류가 발생했습니다."
                TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND -> "인증 토큰이 필요합니다."
                else -> serviceException.getErrorDescription()
            }

        return Response.fail(errorCode, errorMessage)
    }

    @ExceptionHandler(value = [EntityNotFoundException::class])
    fun entityNotFoundHandler(entityNotFoundException: EntityNotFoundException): Response<Any> =
        Response.fail(entityNotFoundException.message ?: "엔티티를 찾을 수 없습니다.")

    @ExceptionHandler(value = [RuntimeException::class])
    fun unknownHandler(runtimeException: RuntimeException): Response<Any> =
        Response.fail(
            runtimeException.message
                ?: "알 수 없는 서버 오류",
        )
}
