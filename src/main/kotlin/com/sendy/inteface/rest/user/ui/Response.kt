package com.sendy.inteface.rest.user.ui

import com.common.domain.error.ErrorCode

data class Response<T>(val code: Int, val message :String, val value:T?=null) {

    companion object {
        fun <T> ok(value:T) : Response<T> {
            return Response(code = 200, message = "OK", value = value)
        }

        fun <T> error(error: ErrorCode, message:String): Response<T> =
           Response(error.code, error.message, null)
    }
}