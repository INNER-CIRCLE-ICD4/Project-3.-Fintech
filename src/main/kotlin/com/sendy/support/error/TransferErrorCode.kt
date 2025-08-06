package com.sendy.support.error

import org.springframework.http.HttpStatus

enum class TransferErrorCode(
    override val httpStatusCode: Int,
    override val errorCode: Int,
    override val description: String,
) : ErrorCodeIfs {
    INVALID_ACCOUNT_NUMBER_PASSWORD(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.value(), "계좌비밀번호가 일치하지 않습니다."),
    NOT_SUFFICIENT_SEND_MONEY(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.value(), "계좌 금액이 충분치 않습니다."),
    IN_ACTIVE_ACCOUNT(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.value(), "계좌가 활성화 되어있지 않습니다."),
    DAILY_MAX_LIMIT(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.value(), "일일 송금 최대 한도가 초과 되었습니다."),
    PAST_NOT_TRANSFER(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.value(), "현재 기준 과거 일자로는 송금을 보낼 수 없습니다."),
    INVALID_SINGLE_TRANSACTION_LIMIT(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.value(),
        "1회 송금 최대 한도가 초과되었습니다.",
    ),
    NOT_FOUND_SENDER_ACCOUNT(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.value(),
        "송금자의 계좌를 찾을 수 없습니다.",
    ),
    NOT_FOUND_RECEIVER_ACCOUNT(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.value(),
        "수취자의 계좌를 찾을 수 없습니다.",
    ),
    INVALID_RECEIVER_PHONE_NUMBER(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.value(),
        "수취자의 휴대 전화 번호가 유효하지 않습니다.",
    ),
}
