package com.sendy.sharedKafka.event.user.email

import java.time.Instant

data class EmailVerificationSentEvent(
    val userId: Long,
    val email: String,
    val username: String,
    val verificationToken: String,
    val expiresAt: Instant
)

data class EmailVerificationSucceedEventHandler(
    val userId: Long,
    val email: String,
    val username: String,
    val verificationToken: String,
    val expiresAt: Instant
)