package com.common.domain.error

enum class ErrorCode(
    override val httpStatusCode: Int,
    override val errorCode: Int,
    override val description: String
) : ErrorCodeIfs {
    OK(200, 200, "성공"),
    BAD_REQUEST(400, 400, "잘못된 요청"),
    SERVER_ERROR(500, 500, "서버에러"),
    NULL_POINT(500, 512, "Null point"),
    NOT_FOUND(404, 404, "찾을 수 없음"),
    INVALID_INPUT_VALUE(400, 400, "유효하지 않은 입력값")
}