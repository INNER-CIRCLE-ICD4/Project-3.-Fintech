package com.sendy.support.response

import com.sendy.support.error.ErrorCode

data class Response<T>(
    val code: Int,
    val message: String,
    val value: T? = null,
) {
    companion object {
        fun <T> ok(value: T): Response<T> = Response(code = 200, message = "OK", value = value)

        fun <T> error(
            errorCode: ErrorCode,
            message: String,
        ): Response<T> = Response(errorCode.errorCode, errorCode.description, null)

        fun <T> fail(message: String): Response<T> = Response(code = 400, message = message, value = null)
    }
}
