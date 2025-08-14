package com.sendy.sendyLegacyApi.support.exception

import com.sendy.sendyLegacyApi.support.error.ErrorCodeIfs

interface ServiceExceptionIfs {
    fun getErrorCodeIfs(): ErrorCodeIfs?

    fun getErrorDescription(): String?
}
