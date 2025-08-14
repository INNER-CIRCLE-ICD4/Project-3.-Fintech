package com.sendy.sendyLegacyApi.support.exception.transfer

class DailyMaxLimitException(
    amount: Long,
    remain: Long,
) : RuntimeException("일일 송금 최대 한도가 초과 되었습니다. 송금액: $amount, 남은 한도: $remain")
