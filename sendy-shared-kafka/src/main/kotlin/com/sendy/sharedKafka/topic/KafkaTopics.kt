package com.sendy.sharedKafka.topic

object KafkaTopics {
    // 회원가입 완료
    const val eventRegistered = "user-api.user.register.email"

    // 이메일 인증
    const val eventVerfitied = "user-api.user.verified.email"

    // 이메일 인증 성공 알림
    const val eventVerifiedSucceed = "user-api.user.verifiedSucceed.email"

    // 회원 비밀번호 변경
    const val eventChangedPassword = "user-api.user.changed.password"
    const val eventTransfered = "transfer-user.transfer.transfer.created"
    const val eventReservedTransfered = "transfer-scheduler.transfer.reservation.created"

    const val eventTransferSucceed = "transfer-api.transfer.transfer.succeed"
    const val eventTransferFail = "transfer-api.transfer.transfer.failed"
}
