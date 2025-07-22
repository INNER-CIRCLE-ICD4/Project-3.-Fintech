package com.sendy.domain.enum

enum class TokenStatus {
    ACTIVE, // 정상 사용 가능한 토큰
    PENDING_LOGOUT, // 다른 디바이스에서 로그인하여 로그아웃 대기 중
    REVOKED, // 완전히 무효화된 토큰
}
