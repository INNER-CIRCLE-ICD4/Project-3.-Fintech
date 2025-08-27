package com.sendy.sharedMongoDB.notification.domain

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "notifications")
class VerifiedNotification(
    @Id
    val notificationId: Long,
    val userId: Long,
    val userName: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val createdAt: Instant = Instant.now(),
    val isRead: Boolean = false,

) {
}

//추후 상황에 맞게 추가 필요
enum class NotificationType {
    WELCOME,           // 환영 메시지
    EMAIL_VERIFICATION, // 이메일 인증
    SYSTEM,            // 시스템 알림
    TRANSFER           // 송금 관련
}