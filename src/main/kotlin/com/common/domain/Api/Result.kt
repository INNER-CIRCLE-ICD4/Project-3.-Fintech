package com.common.domain.Api

import com.common.domain.error.ErrorCode
import com.common.domain.error.ErrorCodeIfs

data class Result(
    val resultCode: Int? = null,
    val resultMessage: String? = null,
    val resultDescription: String? = null
) {
    companion object {
        fun OK(): Result {
            return Result(
                resultCode = ErrorCode.OK.errorCode,
                resultMessage = ErrorCode.OK.description,
                resultDescription = "성공"
            )
        }

        fun ERROR(errorCodeIfs: ErrorCodeIfs): Result {
            return Result(
                resultCode = errorCodeIfs.errorCode,
                resultMessage = errorCodeIfs.description,
                resultDescription = "에러"
            )
        }

        fun ERROR(errorCodeIfs: ErrorCodeIfs, tx: Throwable): Result {
            return Result(
                resultCode = errorCodeIfs.errorCode,
                resultMessage = errorCodeIfs.description,
                resultDescription = "에러"
            )
        }

        fun ERROR(errorCodeIfs: ErrorCodeIfs, description: String): Result {
            return Result(
                resultCode = errorCodeIfs.errorCode,
                resultMessage = errorCodeIfs.description,
                resultDescription = description
            )
        }
    }
}