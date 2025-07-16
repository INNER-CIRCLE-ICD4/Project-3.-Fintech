package com.common.domain.exceptions

import com.common.domain.error.ErrorCode

class CustomException (
    val errorCode: ErrorCode,
    override val message: String = errorCode.message
) : RuntimeException(message)