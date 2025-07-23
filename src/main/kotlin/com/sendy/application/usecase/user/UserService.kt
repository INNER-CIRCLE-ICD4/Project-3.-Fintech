package com.sendy.application.usecase.user

import com.sendy.application.dto.user.RegisterUserRequestDto
import com.sendy.application.dto.user.RegisterUserResponseDto
import com.sendy.application.dto.user.UpdateUserRequestDto
import com.sendy.domain.auth.UserRepository
import com.sendy.domain.auth.token.service.TokenService
import com.sendy.domain.email.EmailJpaRepository
import com.sendy.domain.token.service.TokenService
import com.sendy.email.model.EmailDto
import com.sendy.email.repository.EmailJpaRepository
import com.sendy.support.ResponseException
import com.sendy.support.util.getTsid
import com.sendy.user.domain.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val emailJpaRepository: EmailJpaRepository,
    private val mailSender: JavaMailSender,
    private val builder: AuthenticationManagerBuilder,
    private val tokenService: TokenService,
) {
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

        val userEntity = userRepository.save(entity)
        val sendEmail = sendVerificationEmail(userEntity.email)

        return RegisterUserResponseDto(
            userId = userEntity.id,
            message = userEntity.email + " / " + sendEmail + "인증코드 발송 완료",
        )
    }

    @Transactional
    fun updateUser(
        token: String,
        updateDto: UpdateUserRequestDto,
    ): RegisterUserResponseDto {
        val userId = tokenService.validationToken(token)
        val updateUser = userRepository.findByUserId(userId) ?: throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND)

        updateUser.update(updateDto)

        return RegisterUserResponseDto(
            userId = updateUser.userId,
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
            userRepository.findByEmailAndPassword(email, password) ?: throw ResponseException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND)
        if (userId.equals(userId) == false) {
            throw ResponseException("사용자 정보가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED)
        }
        updateUser.deleteUser()

        return updateUser.email + "삭제 완료"
    }

    // 이메일 발송 로직
    @Transactional
    fun sendVerificationEmail(email: String): String {
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
            ).toEntity()

        val result = emailJpaRepository.save(emailEntity)
        // 중복 이메일 여부 체크?

        // 예: 이메일 서비스 호출
        return result.code
    }

    @Transactional
    fun verifyEmail(
        email: String,
        emailCode: String,
    ): String {
        val emailEntity = emailJpaRepository.findByEmail(email) ?: throw ResponseException("이메일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND)

        if (emailEntity.code != emailCode) {
            throw ResponseException("인증 코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST)
        }

        emailEntity.isVerified = true
        userRepository.findByEmail(email)?.emailVerified = true
        emailJpaRepository.save(emailEntity)

        // 이메일 인증 완료 후 엔티티 삭제
        emailJpaRepository.deleteByEmail(emailEntity)
        return "이메일 인증이 완료되었습니다."
    }
}
