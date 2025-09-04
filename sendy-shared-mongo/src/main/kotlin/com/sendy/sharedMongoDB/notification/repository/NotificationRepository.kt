package com.sendy.sharedMongoDB.notification.repository

import com.sendy.sharedMongoDB.notification.domain.VerifiedNotification
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : MongoRepository<VerifiedNotification, String> {
    fun findByUserIdOrderByCreatedAtDesc(userId: Long): List<VerifiedNotification>
    fun findByUserIdAndIsReadFalse(userId: Long): List<VerifiedNotification>
    fun countByUserIdAndIsReadFalse(userId: Long): Long
}