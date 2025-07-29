package com.sendy.support.exception.transfer

class PastNotTransferException(
    message: String = "현재 기준 과거 일자로는 송금을 보낼 수 없습니다.",
) : RuntimeException(message)
