package com.sendy.consumer.application.usecase

import com.sendy.sharedMongoDB.notification.domain.NotificationType
import com.sendy.sharedMongoDB.notification.domain.VerifiedNotification
import com.sendy.sharedMongoDB.notification.repository.NotificationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.random.Random

@Service
class NotificationEventService(
    private val notificationRepository: NotificationRepository,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun createNotification(
        userId: Long,
        userName: String,
        type: NotificationType,
        title: String,
        message: String,
        notificationId: Long = generateNotificationId(),
        createdAt: Instant = Instant.now(),
        deletedAt: Instant = Instant.now().plus(90, ChronoUnit.DAYS),
    ): VerifiedNotification {
        log.info("알림 생성 시작 - 사용자: {}, 타입: {}", userId, type)

        val notification =
            VerifiedNotification(
                notificationId = notificationId,
                userId = userId,
                userName = userName,
                type = type,
                title = title,
                message = message,
                createdAt = createdAt,
                deletedAt = deletedAt,
                isRead = false,
            )

        return notificationRepository.save(notification).also {
            log.info(
                " MongoDB에 알림 저장 완료 - ID: {}, 사용자: {}",
                it.notificationId,
                it.userId,
            )
        }
    }

    private fun generateNotificationId(): Long = System.currentTimeMillis() + Random.nextInt(1000)
}
