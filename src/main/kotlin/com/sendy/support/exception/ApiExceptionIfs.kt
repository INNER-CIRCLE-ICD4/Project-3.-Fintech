package com.sendy.support.exception

import com.sendy.support.error.ErrorCodeIfs

interface ApiExceptionIfs {
    fun getErrorCodeIfs(): ErrorCodeIfs?

    fun getErrorDescription(): String?
}
