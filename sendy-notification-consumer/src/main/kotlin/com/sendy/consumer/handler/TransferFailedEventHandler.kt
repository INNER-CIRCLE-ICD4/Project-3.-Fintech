package com.sendy.consumer.handler

import com.sendy.consumer.application.usecase.NotificationEventService
import com.sendy.sharedKafka.event.user.email.TransferCompletionEventHandler
import com.sendy.sharedMongoDB.notification.domain.NotificationType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class TransferFailedEventHandler(
    private val notificationEventService: NotificationEventService,
) : EventHandlerConsumer<TransferCompletionEventHandler> {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun handle(event: TransferCompletionEventHandler) {
        val notification =
            notificationEventService.createNotification(
                userId = event.userId,
                userName = event.username,
                type = NotificationType.EMAIL_VERIFICATION,
                title = "송금에 실패하셨습니다.",
                message = "${event.username}님,  송금 실패하셨습니다!.",
                notificationId = System.currentTimeMillis(),
                createdAt = Instant.now(),
            )
        log.info(
            "송금을 안전하게 보냈습니다. 사용자:{}, 알림ID: {}",
            event.username,
            notification.notificationId,
        )
    }

    override fun supports(eventType: String): Boolean = eventType == "TRANSFER_FAILED"
}
