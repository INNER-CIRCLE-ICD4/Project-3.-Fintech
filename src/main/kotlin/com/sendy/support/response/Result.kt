package com.sendy.support.response

import com.sendy.support.error.ErrorCodeIfs
import org.springframework.http.HttpStatus

data class Result(
    val resultCode: Int? = null,
    val resultMessage: String? = null,
    val resultDescription: String? = null,
) {
    companion object {
        fun ok(): Result =
            Result(
                resultCode = HttpStatus.OK.value(),
                resultMessage = HttpStatus.OK.reasonPhrase,
                resultDescription = "성공",
            )

        fun fail(errorCodeIfs: ErrorCodeIfs): Result =
            Result(
                resultCode = errorCodeIfs.errorCode,
                resultMessage = errorCodeIfs.description,
                resultDescription = "에러",
            )

        fun fail(
            errorCodeIfs: ErrorCodeIfs,
            tx: Throwable,
        ): Result =
            Result(
                resultCode = errorCodeIfs.errorCode,
                resultMessage = errorCodeIfs.description,
                resultDescription = "에러",
            )

        fun fail(
            errorCodeIfs: ErrorCodeIfs,
            description: String,
        ): Result =
            Result(
                resultCode = errorCodeIfs.errorCode,
                resultMessage = errorCodeIfs.description,
                resultDescription = description,
            )
    }
}
