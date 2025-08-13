package com.sendy.sendyLegacyApi.support.exception.transfer

class SingleTxLimitException(
    limit: Long,
) : RuntimeException("1회 송금 최대 한도가 초과되었습니다. 최대한도: $limit")
