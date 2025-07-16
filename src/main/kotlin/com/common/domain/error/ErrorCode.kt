package com.common.domain.error

enum class ErrorCode(val code: Int, val message: String) {
    INVALID_INPUT_VALUE(400, "invalid input value"),
    NOT_FOUND(404, "not found data"),
    INTERNAL_ERROR(500, "unexpected error")
}