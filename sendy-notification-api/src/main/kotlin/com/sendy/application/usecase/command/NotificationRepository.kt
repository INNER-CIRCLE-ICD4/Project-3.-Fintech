package com.sendy.application.usecase.command

import com.sendy.Notification

interface NotificationRepository {
    fun findById(id:String): Notification?

    fun save(notification: Notification)

    fun delete(id: String)
}