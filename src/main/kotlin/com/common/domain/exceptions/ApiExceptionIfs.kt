package com.common.domain.exceptions

import com.common.domain.error.ErrorCodeIfs

interface ApiExceptionIfs {

    fun getErrorCodeIfs(): ErrorCodeIfs?

    fun getErrorDescription(): String?
}