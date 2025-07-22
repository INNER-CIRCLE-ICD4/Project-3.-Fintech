package com.sendy.support.response

import com.sendy.support.error.ErrorCode
import com.sendy.support.error.ErrorCodeIfs

data class Result(
    val resultCode: Int? = null,
    val resultMessage: String? = null,
    val resultDescription: String? = null,
) {
    companion object {
        fun OK(): Result =
            Result(
                resultCode = ErrorCode.OK.errorCode,
                resultMessage = ErrorCode.OK.description,
                resultDescription = "성공",
            )

        fun ERROR(errorCodeIfs: ErrorCodeIfs): Result =
            Result(
                resultCode = errorCodeIfs.errorCode,
                resultMessage = errorCodeIfs.description,
                resultDescription = "에러",
            )

        fun ERROR(
            errorCodeIfs: ErrorCodeIfs,
            tx: Throwable,
        ): Result =
            Result(
                resultCode = errorCodeIfs.errorCode,
                resultMessage = errorCodeIfs.description,
                resultDescription = "에러",
            )

        fun ERROR(
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
