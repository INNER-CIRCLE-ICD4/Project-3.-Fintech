package com.sendy.sendyLegacyApi.support.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    override val httpStatusCode: Int,
    override val errorCode: Int,
    override val description: String,
) : ErrorCodeIfs {
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.value(), "잘못된 요청"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에러"),
    NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), 512, "Null point"),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.value(), "찾을 수 없음"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.value(), "유효하지 않은 입력값"),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.value(), "요청이 너무 많습니다"),
    PASSWORD_ERROR_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS.value(), 1001, "비밀번호 오류 횟수 초과"),
}
