package com.sendy.consumer.handler

import com.sendy.consumer.application.usecase.NotificationEventService
import com.sendy.sharedKafka.event.user.email.EmailVerificationSentEvent
import com.sendy.sharedMongoDB.notification.domain.NotificationType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class EmailVerifiedEventHandler(
    private val notificationEventService: NotificationEventService,
): EventHandlerConsumer<EmailVerificationSentEvent> {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun handle(event: EmailVerificationSentEvent) {
       val notification = notificationEventService.createNotification(
           userId = event.userId,
           userName = event.username,
           type = NotificationType.EMAIL_VERIFICATION,
           title = "인증 이메일 발송되었습니다.",
           message = "${event.userId}님, ${event.email}로 인증 링크를 발송했습니다. 이메일을 확인해주세요!.",
           notificationId = System.currentTimeMillis(),
           createdAt = Instant.now(),
       )
        log.info("이메일 인증 알람 생성 완료! 사용자:{}, 알림ID: {}",
            event.userId, notification.notificationId)
    }

    override fun supports(eventType: String): Boolean {
        return eventType == "USER_VERIFICATION"
    }
}