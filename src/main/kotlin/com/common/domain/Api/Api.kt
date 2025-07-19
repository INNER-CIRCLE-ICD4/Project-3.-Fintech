package com.common.domain.Api

import com.common.domain.error.ErrorCodeIfs
import jakarta.validation.Valid

data class Api<T>(
    var result: Result? = null,
    @field:Valid
    var body: T? = null
) {
    companion object {
        fun <T> OK(data: T): Api<T> {
            val api = Api<T>()
            api.result = Result.OK()
            api.body = data
            return api
        }

        fun ERROR(result: Result): Api<Any> {
            val api = Api<Any>()
            api.result = result
            return api
        }

        fun ERROR(errorCodeIfs: ErrorCodeIfs): Api<Any> {
            val api = Api<Any>()
            api.result = Result.ERROR(errorCodeIfs)
            return api
        }

        fun ERROR(errorCodeIfs: ErrorCodeIfs, tx: Throwable): Api<Any> {
            val api = Api<Any>()
            api.result = Result.ERROR(errorCodeIfs, tx)
            return api
        }

        fun ERROR(errorCodeIfs: ErrorCodeIfs, description: String): Api<Any> {
            val api = Api<Any>()
            api.result = Result.ERROR(errorCodeIfs, description)
            return api
        }
    }
}