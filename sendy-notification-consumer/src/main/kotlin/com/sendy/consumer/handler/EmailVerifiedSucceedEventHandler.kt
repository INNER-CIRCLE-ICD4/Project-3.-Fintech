package com.sendy.consumer.handler

import com.sendy.consumer.application.usecase.NotificationEventService
import com.sendy.sharedKafka.event.user.email.EmailVerificationSucceedEventHandler
import com.sendy.sharedMongoDB.notification.domain.NotificationType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class EmailVerifiedSucceedEventHandler(
    private val notificationEventService: NotificationEventService,
): EventHandlerConsumer<EmailVerificationSucceedEventHandler> {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun handle(event: EmailVerificationSucceedEventHandler) {
        val notification = notificationEventService.createNotification(
            userId = event.userId,
            userName = event.username,
            type = NotificationType.EMAIL_VERIFICATION,
            title = "이메일 인증에 성공하셨습니다.",
            message = "${event.username}님,  인증 성공하셨습니다!.",
            notificationId = System.currentTimeMillis(),
            createdAt = Instant.now(),
        )
        log.info("이메일 인증 성공 알림 생성 완료! 사용자:{}, 알림ID: {}",
            event.username, notification.notificationId)
    }

    override fun supports(eventType: String): Boolean {
        return eventType == "USER_VERIFICATION_SUCCESS"
    }
}