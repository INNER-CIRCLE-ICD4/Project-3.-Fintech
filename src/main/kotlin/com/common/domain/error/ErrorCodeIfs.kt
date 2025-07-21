package com.common.domain.error

interface ErrorCodeIfs {
    val httpStatusCode: Int?

    val errorCode: Int?

    val description: String?
}