package com.sendy.support.error

import org.springframework.http.HttpStatus

enum class TransferErrorCode(
    override val httpStatusCode: Int,
    override val errorCode: Int,
    override val description: String,
) : ErrorCodeIfs {
    INVALID_ACCOUNT_NUMBER_PASSWORD(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.value(), "계좌비밀번호가 일치하지 않습니다."),
}
