package com.sendy.sharedKafka.event

import java.time.LocalDateTime

data class EventMessage<T>(
    val id: String,
    val source: String,
    val aggregateId: String,
    val payload: T,
    val type: String,
    val createdAt: String,
)

object EventTypes {

    //회원가입 관련 이벤트
    const val USER_REGISTRATION = "USER_REGISTRATION"
    const val USER_VERIFICATION = "USER_VERIFICATION"
    const val PASSWORD_CHANGED = "PASSWORD_CHANGED"

    //유저 관련 이벤트
    const val LOGIN_FAILED = "LOGIN_FAILED"

    //계좌 관련 이벤트
    const val ACCOUNT_CREATED = "ACCOUNT_CREATED"
    const val ACCOUNT_DELETED = "ACCOUNT_DELETED"
    const val TRANSFER_COMPLETED = "TRANSFER_COMPLETED"
    const val TRANSFER_FAILED = "TRANSFER_FAILED"


}