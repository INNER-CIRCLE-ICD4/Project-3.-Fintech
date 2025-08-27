package com.sendy.application.usecase

import com.sendy.application.dto.NotificationResponse
import com.sendy.sharedMongoDB.notification.domain.VerifiedNotification
import com.sendy.sharedMongoDB.notification.repository.NotificationRepository
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
) {

        fun getEmailVerifiedNotifications(userId:Long): List<VerifiedNotification> {
        return notificationRepository.findByUserIdAndIsReadFalse(userId)
    }

}
class NotificationNotFoundException(notificationId: String) :
    RuntimeException("Notification not found: $notificationId")