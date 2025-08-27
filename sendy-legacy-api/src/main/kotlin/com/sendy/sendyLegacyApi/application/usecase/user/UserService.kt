package com.sendy.sendyLegacyApi.application.usecase.user

import com.sendy.sendyLegacyApi.application.dto.email.EmailDto
import com.sendy.sendyLegacyApi.application.dto.user.CreateUserDto
import com.sendy.sendyLegacyApi.application.dto.user.UpdateUserDto
import com.sendy.sendyLegacyApi.domain.auth.UserEntityRepository
import com.sendy.sendyLegacyApi.domain.auth.UserRepository
import com.sendy.sendyLegacyApi.domain.user.UserEntity
import com.sendy.sendyLegacyApi.infrastructure.persistence.email.EmailRepository
import com.sendy.sendyLegacyApi.support.exception.ResponseException
import com.sendy.sendyLegacyApi.support.response.Result
import com.sendy.sendyLegacyApi.support.util.Aes256Util
import com.sendy.sendyLegacyApi.support.util.SHA256Util
import com.sendy.sendyLegacyApi.support.util.getTsid
import com.sendy.sharedKafka.event.EventMessage
import com.sendy.sharedKafka.event.EventPublisher
import com.sendy.sharedKafka.event.EventTypes
import com.sendy.sharedKafka.event.user.email.EmailVerificationSentEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userEntityRepository: UserEntityRepository,
    private val emailRepository: EmailRepository,
    private val mailSender: JavaMailSender,
    private val sha256Util: SHA256Util,
    private val mailAsyncSend: MailAsyncSend,
    private val eventPublisher: EventPublisher, // Kafka EventPublisher 추가
    @Value("\${aes256.key}") private val key: String,
) {
    private val aesUtil = Aes256Util(key)

    /**
     * 유저 등록
     * @param requestDto 유저 등록 요청 DTO
     */
    @Transactional
    fun registerUser(requestDto: CreateUserDto): UserEntity {
        // ci 값 등록
        val entity = requestDto.toEntity(getTsid(), sha256Util.hash(requestDto.password))
        return userEntityRepository.save(entity)
    }

    @Transactional
    fun findUserId(email: String): Long {
        val user =
            userEntityRepository.findByEmail(email).get()
                ?: throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND)
        // 사용자 ID 반환
        return user.id
    }

    @Transactional
    fun updateUser(
        id: Long,
        updateDto: UpdateUserDto,
    ): UserEntity {
        val updateUser =
            userEntityRepository
                .findByIdAndIsDeleteFalse(id)
                .orElseThrow { throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND) }

        return updateUser.update(updateDto)
    }

    @Transactional
    fun deleteUser(
        email: String,
        password: String,
        id: Long,
    ): UserEntity {
        val delUser =
            userEntityRepository
                .findByIdAndIsDeleteFalse(id)
                .orElseThrow { throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND) }

        if (!delUser.email.equals(email) || !delUser.password.equals(password)) {
            throw ResponseException("사용자 정보가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED)
        }

        if (delUser.isDelete) {
            throw ResponseException("이미 삭제된 사용자입니다.", HttpStatus.NOT_FOUND)
        }

        return delUser.deleteUser()
    }

    // 이메일 발송 로직
    @Transactional
    fun sendVerificationEmail(
        email: String,
        userId: Long,
    ): String {
        var randomCode = ""
        try {
            randomCode = (1..6).map { (0..9).random() }.joinToString("")
            val emailEntity =
                EmailDto(
                    id = getTsid(),
                    code = randomCode,
                    email = email,
                    isVerified = false,
                    userId = userId,
                    sendAt = java.time.LocalDateTime.now(),
                ).toEntity()
            emailRepository.save(emailEntity)
        } catch (e: Exception) {
            throw ResponseException("이메일 발송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
        }

        // 실제 이메일 발송
        val mailFlag = mailAsyncSend.sendEmailAsync(mailSender, email, randomCode)
        
        // 이메일 발송 후 Kafka 알림 메시지 발송
        val user = userEntityRepository.findById(userId).orElseThrow { 
            throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND) 
        }
        
        val eventMessage = EventMessage(
            id = getTsid().toString(),
            source = "sendy-legacy-api",
            aggregateId = userId.toString(),
            payload = EmailVerificationSentEvent(
                userId = userId,
                email = email,
                username = user.name,
                verificationToken = randomCode,
                expiresAt = Instant.now().plusSeconds(3600)
            ),
            type = EventTypes.USER_VERIFICATION,
            createdAt = java.time.LocalDateTime.now().toString()
        )
        
        eventPublisher.publish(
            topic = "user-registration.user.register.email",
            key = email,
            data = eventMessage,
        )
        
        return "인증 코드 발송" + mailFlag
    }

    @Transactional
    fun verifyEmail(
        email: String,
        emailCode: String,
    ): Result {
        val emailEntity = emailRepository.findByEmail(email) ?: throw ResponseException("이메일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND)
        val user =
            userEntityRepository.findById(emailEntity.userId).orElseThrow {
                throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND)
            }
        if (!user.email.equals(email)) {
            throw ResponseException("이메일 정보가 다릅니다.", HttpStatus.NOT_FOUND)
        }

        if (emailEntity.code != emailCode) {
            throw ResponseException("인증 코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST)
        }
        user.emailVerified = true
        emailEntity.isVerified = true
        emailRepository.save(emailEntity)

        return Result(200, "이메일 인증 성공")
    }
}
