package com.sendy.sendyLegacyApi.support.exception

import com.sendy.sendyLegacyApi.support.error.ErrorCodeIfs

class ServiceException :
    RuntimeException,
    ServiceExceptionIfs {
    private val errorCodeIfs: ErrorCodeIfs
    private val errorDescription: String

    constructor(errorCodeIfs: ErrorCodeIfs) : super(errorCodeIfs.description) {
        this.errorCodeIfs = errorCodeIfs
        this.errorDescription = errorCodeIfs.description ?: ""
    }

    constructor(errorCodeIfs: ErrorCodeIfs, errorDescription: String) : super(errorDescription) {
        this.errorCodeIfs = errorCodeIfs
        this.errorDescription = errorDescription
    }

    constructor(errorCodeIfs: ErrorCodeIfs, tx: Throwable) : super(tx) {
        this.errorCodeIfs = errorCodeIfs
        this.errorDescription = errorCodeIfs.description ?: ""
    }

    constructor(errorCodeIfs: ErrorCodeIfs, tx: Throwable, errorDescription: String) : super(tx) {
        this.errorCodeIfs = errorCodeIfs
        this.errorDescription = errorDescription
    }

    override fun getErrorCodeIfs(): ErrorCodeIfs = errorCodeIfs

    override fun getErrorDescription(): String = errorDescription
}
