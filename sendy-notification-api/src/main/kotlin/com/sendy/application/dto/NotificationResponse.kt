package com.sendy.application.dto

import com.sendy.sharedMongoDB.notification.domain.NotificationType
import com.sendy.sharedMongoDB.notification.domain.VerifiedNotification
import java.time.Instant

data class NotificationResponse(
    val id: Long,
    val userId: Long,
    val type: NotificationType,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val createdAt: Instant,
    val deletedAt: Instant?,
)

data class UnreadCountResponse(
    val count: Long,
)

// 확장 함수
fun VerifiedNotification.toResponse(): NotificationResponse =
    NotificationResponse(
        id = this.notificationId,
        userId = this.userId,
        type = this.type,
        title = this.title,
        message = this.message,
        isRead = this.isRead,
        createdAt = this.createdAt,
        deletedAt = this.deletedAt,
    )
