package com.sendy

import java.time.Instant

class Notification(
    var id:String,
    var userId:Long,
    var type:NotificationType,
    var createdAt: Instant,
    var deletedAt: Instant?,
){

    enum class NotificationType {
        REGISTER, //회원가입
        TRANSFER, //송금
        PW_CHANGED, //비밀번호 변경
        ACCOUNT_CREATED, //계좌 생성
        ACCOUNT_DELETED, //계좌 삭제

    }

}