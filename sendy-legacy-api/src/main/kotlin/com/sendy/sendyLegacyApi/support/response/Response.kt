package com.sendy.sendyLegacyApi.support.response

import com.sendy.sendyLegacyApi.support.error.ErrorCode
import com.sendy.sendyLegacyApi.support.error.ErrorCodeIfs
import jakarta.validation.Valid

data class Response<T>(
    var result: Result? = null,
    @field:Valid
    var data: T? = null,
) {
    companion object {
        fun <T> ok(data: T): Response<T> {
            val response = Response<T>()
            response.result = Result.ok()
            response.data = data
            return response
        }

        fun fail(result: Result): Response<Any> {
            val response = Response<Any>()
            response.data = result
            return response
        }

        fun fail(errorCodeIfs: ErrorCodeIfs): Response<Any> {
            val response = Response<Any>()
            response.result = Result.fail(errorCodeIfs)
            return response
        }

        fun fail(
            errorCodeIfs: ErrorCodeIfs,
            tx: Throwable,
        ): Response<Any> {
            val response = Response<Any>()
            response.result = Result.fail(errorCodeIfs, tx)
            return response
        }

        fun fail(
            errorCodeIfs: ErrorCodeIfs,
            description: String,
        ): Response<Any> {
            val response = Response<Any>()
            response.result = Result.fail(errorCodeIfs, description)
            return response
        }

        fun <T> fail(message: String): Response<T> = fail(ErrorCode.BAD_REQUEST, message) as Response<T>
    }
}
