package com.sendy.sendyLegacyApi.support.error

interface ErrorCodeIfs {
    val httpStatusCode: Int?

    val errorCode: Int?

    val description: String?
}
