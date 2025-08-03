package com.sendy.support.response

import com.sendy.support.error.ErrorCode
import com.sendy.support.error.ErrorCodeIfs
import jakarta.validation.Valid

data class Api<T>(
    var result: Result? = null,
    @field:Valid
    var body: T? = null,
) {
    companion object {
        // 기존 Api 메서드들
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

        fun ERROR(
            errorCodeIfs: ErrorCodeIfs,
            tx: Throwable,
        ): Api<Any> {
            val api = Api<Any>()
            api.result = Result.ERROR(errorCodeIfs, tx)
            return api
        }

        fun ERROR(
            errorCodeIfs: ErrorCodeIfs,
            description: String,
        ): Api<Any> {
            val api = Api<Any>()
            api.result = Result.ERROR(errorCodeIfs, description)
            return api
        }

        // Response.kt와 호환되는 간편한 메서드들
        fun <T> ok(value: T): Api<T> = OK(value)
        
        fun <T> error(errorCode: ErrorCode, message: String): Api<Any> =
            Api.ERROR(errorCode, message)
        
        fun <T> fail(message: String): Api<Any> =
            Api.ERROR(ErrorCode.BAD_REQUEST, message)
    }
}
