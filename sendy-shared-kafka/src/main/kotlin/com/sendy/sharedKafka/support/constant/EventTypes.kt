package com.sendy.sharedKafka.support.constant

object EventTypes {
    /** 네이밍 규칙: [도메인]_[액션]_[상태]
     * - 도메인: USER, ACCOUNT, TRANSFER, EMAIL, NOTIFICATION 등
     * - 액션: REGISTRATION, VERIFICATION, CREATION, COMPLETION 등
     * - 상태: INITIATED, COMPLETED, FAILED, SENT, RECEIVED 등
     *
     * **/
    // 회원가입 관련 이벤트
    const val USER_REGISTRATION = "USER_REGISTRATION"
    const val USER_VERIFICATION = "USER_VERIFICATION"
    const val USER_VERIFICATION_SUCCESS = "USER_VERIFICATION_SUCCESS"

    const val PASSWORD_CHANGED = "PASSWORD_CHANGED"

    // 유저 관련 이벤트
    const val LOGIN_FAILED = "LOGIN_FAILED"

    // 계좌 관련 이벤트
    const val ACCOUNT_CREATED = "ACCOUNT_CREATED"
    const val ACCOUNT_DELETED = "ACCOUNT_DELETED"
    const val ACCOUNT_CLOSED = "ACCOUNT_CLOSED"

    // 송금
    const val TRANSFER_INITIATED = "TRANSFER_INITIATED"
    const val TRANSFER_VALIDATION_COMPLETED = "TRANSFER_VALIDATION_COMPLETED"
    const val TRANSFER_VALIDATION_FAILED = "TRANSFER_VALIDATION_FAILED" // 송금 인증 실패시 알림발송
    const val TRANSFER_SUCCEED = "TRANSFER_SUCCEED" // 송금 성공
    const val TRANSFER_FAILED = "TRANSFER_FAILED" // 송금 실패
    const val TRANSFER_CANCELLED = "TRANSFER_CANCELLED" // 송금 취소

    // 예약 송금
    const val TRANSFER_RESERVED_COMPLETED = "TRANSFER_COMPLETED" // 송금 성공
    const val TRANSFER_RESERVED_FAILED = "TRANSFER_FAILED" // 송금 실패
    const val TRANSFER_RESERVED_CANCELLED = "TRANSFER_CANCELLED" // 송금 취소
}
