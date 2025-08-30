package com.sendy.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.sendy.consumer.handler.EmailVerifiedEventHandler
import com.sendy.sharedKafka.domain.EventMessage
import com.sendy.sharedKafka.domain.EventTypes
import com.sendy.sharedKafka.event.user.email.EmailVerificationSentEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class NotificationEventConsumer(
    private val objectMapper: ObjectMapper,
    private val emailVerifiedEventHandler: EmailVerifiedEventHandler,
) {
    private val log = LoggerFactory.getLogger(NotificationEventConsumer::class.java)

    @KafkaListener(
        topics = ["user-registration.user.register.email"],
        groupId = "notification-service",
    )
    fun handleUserRegistrationEvents(
        @Payload message: String,
        ack: Acknowledgment,
    ) {
        log.info("원시 메시지 수신: {}", message)

        try {
            val eventMessage = objectMapper.readValue(message, EventMessage::class.java)
            log.info(
                " JSON 역직렬화 성공 - 타입: {}, 사용자: {}, 토픽: user-registration.user.register.email",
                eventMessage.type,
                eventMessage.aggregateId,
            )

            when (eventMessage.type) {
                EventTypes.USER_VERIFICATION -> {
                    val event = eventMessage.payload?.let { convertPayload(it, EmailVerificationSentEvent::class.java) }
                    event?.let { handleEmailVerificationSent(it) }
                }

                else -> {
                    log.warn("처리되지 않는 이벤트 타입: {}", eventMessage.type)
                }
            }

            // 처리 완료 후 수동 커밋
            ack.acknowledge()
        } catch (e: Exception) {
            log.error(" JSON 역직렬화 실패: {}", e.message, e)
            // 에러 발생 시 재처리를 위해 acknowledge 하지 않음
        }
    }

    private fun handleEmailVerificationSent(event: EmailVerificationSentEvent) {
        log.info(
            "이메일 인증 발송 이벤트 처리 - 사용자: {}, 이메일: {}, 토큰: {}",
            event.userId,
            event.email,
            event.verificationToken,
        )

        try {
            // MongoDB에 알림 저장
            emailVerifiedEventHandler.handle(event)

            log.info(" MongoDB 저장 성공!")
            println("Consumer에서 이벤트 처리 완료!")
            println("   - 사용자 ID: ${event.userId}")
            println("   - 이메일: ${event.email}")
            println("   - 사용자명: ${event.username}")
            println("   - 인증 토큰: ${event.verificationToken}")
            println("   - 만료 시간: ${event.expiresAt}")
            println("   -  MongoDB에 알림 저장됨")
        } catch (e: Exception) {
            log.error("MongoDB 저장 실패 - 사용자: {}, 오류: {}", event.userId, e.message, e)
            throw e // 재처리를 위해 예외 다시 던지기
        }
    }

    private fun <T> convertPayload(
        payload: Any,
        targetClass: Class<T>,
    ): T = objectMapper.convertValue(payload, targetClass)
}
