package com.common.domain.error

interface ErrorCodeIfs {
    val getHttpStatusCode: Int
    val getErrorCode: Int
    val getDescription: String
}