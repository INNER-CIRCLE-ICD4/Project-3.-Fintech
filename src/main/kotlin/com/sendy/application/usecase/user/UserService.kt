package com.sendy.domain.service

import com.sendy.application.dto.RegisterUserRequestDto
import com.sendy.application.dto.email.EmailDto
import com.sendy.application.dto.user.RegisterUserResponseDto
import com.sendy.application.dto.user.UpdateUserRequestDto
import com.sendy.domain.auth.UserEntityRepository
import com.sendy.domain.auth.UserRepository
import com.sendy.domain.auth.token.service.TokenService
import com.sendy.infrastructure.persistence.email.EmailRepository
import com.sendy.support.exception.ResponseException
import com.sendy.support.util.Aes256Util
import com.sendy.support.util.getTsid
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userEntityRepository: UserEntityRepository,
    private val emailRepository: EmailRepository,
    private val mailSender: JavaMailSender,
    private val builder: AuthenticationManagerBuilder,
    private val tokenService: TokenService,
    @Value("\${aes256.key}") private val key: String,
) {
    private val aesUtil = Aes256Util(key)

    /**
     * 유저 등록
     * @param requestDto 유저 등록 요청 DTO
     * @return RegisterUserResponseDto 유저 등록 응답 DTO
     */
    @Transactional
    fun registerUser(requestDto: RegisterUserRequestDto): RegisterUserResponseDto {
        // ci 값 등록
        val tsid = getTsid()
        val entity = requestDto.toEntity(tsid)

        val userEntity = userEntityRepository.save(entity)

        return RegisterUserResponseDto(
            userId = userEntity.id,
            message = "회원가입 완료",
        )
    }

    @Transactional
    fun updateUser(
        token: String,
        updateDto: UpdateUserRequestDto,
    ): RegisterUserResponseDto {
        val userId = tokenService.validationToken(token)
        val updateUser =
            userEntityRepository
                .findByIdAndIsDeleteFalse(userId)
                .orElseThrow { throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND) }

        updateUser.update(updateDto)

        return RegisterUserResponseDto(
            userId = updateUser.id,
            message = "수정완료",
        )
    }

    @Transactional
    fun deleteUser(
        email: String,
        password: String,
        token: String,
    ): String {
        val userId = tokenService.validationToken(token)
        val updateUser =
            userEntityRepository
                .existsByEmailAndPassword(email, password)
                .orElseThrow { throw ResponseException("이메일 또는 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED) }
        if (userId.equals(userId) == false) {
            throw ResponseException("사용자 정보가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED)
        }
        updateUser.deleteUser()

        return updateUser.email + "삭제 완료"
    }

    // 이메일 발송 로직
    @Transactional
    fun sendVerificationEmail(
        email: String,
        userId: Long,
    ): String {
        // 테스트 코드 저장
        if (email.equals("test@gmail.com")) {
            val TestEmailEntity =
                EmailDto(
                    emailId = getTsid(),
                    code = "123123",
                    email = email,
                    isVerified = false,
                    userId = userId,
                ).toEntity()
            val result = emailRepository.save(TestEmailEntity)
            return result.code
        }

        val randomCode = (1..6).map { (0..9).random() }.joinToString("")

        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        helper.setTo(email)
        helper.setSubject("회원가입 인증 코드")
        helper.setText("인증 코드: $randomCode", true)
        helper.setFrom("sendy.smtp.test@gmail.com")
        mailSender.send(message)

        val emailEntity =
            EmailDto(
                emailId = getTsid(),
                code = randomCode,
                email = email,
                isVerified = false,
                userId = userId,
            ).toEntity()

        val result = emailRepository.save(emailEntity)
        // 중복 이메일 여부 체크?

        // 예: 이메일 서비스 호출
        return result.code
    }

    @Transactional
    fun verifyEmail(
        email: String,
        emailCode: String,
    ): String {
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

        // 이메일 인증 완료 후 엔티티 삭제?
//        emailRepository.deleteByEmail(emailEntity)
        return "이메일 인증이 완료되었습니다."
    }
}
