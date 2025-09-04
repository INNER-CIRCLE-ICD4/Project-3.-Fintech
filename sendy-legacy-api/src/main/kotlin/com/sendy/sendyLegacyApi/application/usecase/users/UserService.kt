package com.sendy.sendyLegacyApi.application.usecase.users

import com.sendy.sendyLegacyApi.application.dto.email.EmailDto
import com.sendy.sendyLegacyApi.application.dto.users.CreateUserDto
import com.sendy.sendyLegacyApi.application.dto.users.UpdateUserDto
import com.sendy.sendyLegacyApi.domain.authorities.UserEntityRepository
import com.sendy.sendyLegacyApi.domain.email.EmailEntity
import com.sendy.sendyLegacyApi.domain.user.UserEntity
import com.sendy.sendyLegacyApi.infrastructure.persistence.email.EmailRepository
import com.sendy.sendyLegacyApi.support.exception.ResponseException
import com.sendy.sendyLegacyApi.support.response.Result
import com.sendy.sendyLegacyApi.support.util.Aes256Util
import com.sendy.sendyLegacyApi.support.util.SHA256Util
import com.sendy.sendyLegacyApi.support.util.getTsid
import com.sendy.sharedKafka.domain.EventMessageRepository
import com.sendy.sharedKafka.domain.EventPublisher
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserService(
    private val userEntityRepository: UserEntityRepository,
    private val emailRepository: EmailRepository,
    private val mailSender: JavaMailSender,
    private val sha256Util: SHA256Util,
    private val mailAsyncSend: MailAsyncSend,
    private val eventPublisher: EventPublisher, // Kafka EventPublisher 추가
    @Value("\${aes256.key}") private val key: String,
    private val eventMessageRepository: EventMessageRepository,
) {
    private val aesUtil = Aes256Util(key)

    /**
     * 유저 등록
     * @param requestDto 유저 등록 요청 DTO
     */
    @Transactional
    fun registerUser(requestDto: CreateUserDto): String {
        // ci 값 등록
        val entity = requestDto.toEntity(getTsid(), sha256Util.hash(requestDto.password))
        userEntityRepository.save(entity)
        return entity.id.toString()
    }

    @Transactional
    fun findUserId(email: String): Long =
        userEntityRepository
            .findByEmail(email)
            ?.id
            ?: throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND)

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Transactional
    fun updateUser(
        id: Long,
        updateDto: UpdateUserDto,
    ): String {
        val updateUser =
            userEntityRepository
                .findByIdAndIsDeleteFalse(id)
                .orElseThrow { throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND) }

        updateUser.update(updateDto)

        userEntityRepository.save(updateUser)
        return updateUser.id.toString()
    }

    @Transactional
    fun deleteUser(
        password: String,
        id: Long,
    ): String {
        val delUser =
            userEntityRepository
                .findByIdAndIsDeleteFalse(id)
                .orElseThrow { throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND) }

        if (!sha256Util.matches(password, delUser.password)) {
            throw ResponseException("비밀번호 일치하지 않습니다.", HttpStatus.UNAUTHORIZED)
        }

        if (delUser.isDelete) {
            throw ResponseException("이미 삭제된 사용자입니다.", HttpStatus.NOT_FOUND)
        }
        delUser.userDelete()
        delUser.deleteAt = LocalDateTime.now()
        return delUser.id.toString() + "delete success"
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
//        val user =
//            userEntityRepository.findById(userId).orElseThrow {
//                throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND)
//            }

//        eventMessageRepository.saveReady(
//            EventMessage(
//                id = getTsid(),
//                source = "sendy-legacy-api",
//                aggregateId = userId,
//                payload =
//                    EmailVerificationSentEvent(
//                        userId = userId,
//                        email = email,
//                        username = user.name,
//                        verificationToken = randomCode,
//                        expiresAt = Instant.now().plusSeconds(3600),
//                    ),
//                type = EventTypes.USER_VERIFICATION,
//            ),
//        )

        return "인증 코드 발송" + mailFlag
    }

    @Transactional
    fun verifyEmail(
        userId: Long,
        email: String,
        emailCode: String,
    ): Result {
        val userEntity =
            userEntityRepository
                .findById(userId)
                .orElseThrow { throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND) }

        val emailEntitys: List<EmailEntity> =
            emailRepository.findByUserId(userId)
                ?: throw ResponseException("이메일 인증 정보가 존재하지 않습니다.", HttpStatus.BAD_REQUEST)

        // 이메일 테이블에서 인증할 사용자와 같은지 확인
        emailEntitys.forEach {
            if (!it.userId.equals(userEntity.id)) {
                throw ResponseException("사용자 정보가 다릅니다.", HttpStatus.NOT_FOUND)
            }

            if (it.isVerified == false) {
                if (it.code != emailCode) {
                    throw ResponseException("인증 코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST)
                }

                if (userEntity.isLocked) {
                    userEntity.emailVerified = true
                    userEntity.wrongCount = 0
                    userEntity.isLocked = false
                    it.isVerified = true
                } else {
                    userEntity.emailVerified = true
                    it.isVerified = true
                }
                emailRepository.save(it)
                userEntityRepository.save(userEntity)
            }
        }
        return Result(200, "이메일 인증 성공")
    }

    /**
     * 유저 조회
     * @param id 유저 아이디
     */
    @Transactional(readOnly = true)
    fun findUser(id: Long): UserEntity {
        val getUser =
            userEntityRepository
                .findByIdAndIsDeleteFalse(id)
                .orElseThrow { throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND) }

        return getUser
    }
}
