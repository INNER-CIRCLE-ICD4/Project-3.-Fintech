package com.sendy.controller

import com.sendy.application.usecase.NotificationService
import com.sendy.sharedMongoDB.notification.domain.VerifiedNotification
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/notification")
class NotificationController(
    private val notificationService: NotificationService
) {

    @GetMapping("/users/{userId}")
    fun getUserNotifications(@PathVariable userId: Long): List<VerifiedNotification> {
        return notificationService.getEmailVerifiedNotifications(userId)
    }



}