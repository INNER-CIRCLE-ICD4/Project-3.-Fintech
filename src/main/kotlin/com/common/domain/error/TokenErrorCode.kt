package com.common.domain.error


enum class TokenErrorCode(
    override val getDescription: String,
    override val getHttpStatusCode: Int,
    override val getErrorCode: Int,
)
    : ErrorCodeIfs{

    INVALID_TOKEN("유효하지 않은 토큰", 1001, 401),
    EXPIRED_TOKEN("만료된 토큰", 1002, 401),
    TOKEN_EXCEPTION("토큰 예외", 1003, 400),
    AUTHORIZATION_TOKEN_NOT_FOUND("인증 토큰 없음", 1004, 403)
}