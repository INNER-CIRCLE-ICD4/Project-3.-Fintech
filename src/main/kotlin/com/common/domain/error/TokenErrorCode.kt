package com.common.domain.error

enum class TokenErrorCode(
    override val httpStatusCode: Int,
    override val errorCode: Int,
    override val description: String
) : ErrorCodeIfs {
    INVALID_TOKEN(401, 1001, "유효하지 않은 토큰"),
    EXPIRED_TOKEN(401, 1002, "만료된 토큰"),
    TOKEN_EXCEPTION(400, 1003, "토큰 예외"),
    AUTHORIZATION_TOKEN_NOT_FOUND(403, 1004, "인증 토큰 없음")
}