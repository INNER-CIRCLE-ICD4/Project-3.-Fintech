package com.sendy.sendyLegacyApi.interfaces.rest.user

import com.sendy.sendyLegacyApi.application.dto.user.CreateUserDto
import com.sendy.sendyLegacyApi.application.dto.user.UpdateUserDto
import com.sendy.sendyLegacyApi.application.usecase.user.UserService
import com.sendy.sendyLegacyApi.domain.user.UserEntity
import com.sendy.sendyLegacyApi.support.response.Response
import com.sendy.sendyLegacyApi.support.response.Result
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

/**
 *
 * controller
 * 유저 등록하는 컨트롤러
 */
@Tag(name = "User", description = "회원 API")
@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {
    @PostMapping("")
    @Operation(summary = "사용자 등록", description = "사용자 정보로 회원가입을 합니다.")
    fun registerUser(
        @RequestBody @Valid requestDto: CreateUserDto,
    ): Response<UserEntity> = Response.ok(userService.registerUser(requestDto))

    @PutMapping("/{id}")
    @Operation(summary = "사용자 수정", description = "사용자 정보를 수정합니다.")
    fun updateUser(
        @RequestBody @Valid updateDto: UpdateUserDto,
        @PathVariable("id") id: Long,
    ): Response<UserEntity> = Response.ok(userService.updateUser(id, updateDto))

    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 삭제", description = "사용자 정보를 삭제합니다.")
    fun deleteUser(
        email: String,
        password: String,
        @PathVariable("id") id: Long,
    ): Response<UserEntity> {
        // 로그인된 사용자 엔티티 조회
        return Response.ok(userService.deleteUser(email, password, id))
    }

    @PostMapping("/auth/email/send")
    @Operation(summary = "본인인증 이메일 발송", description = "회원가입시 본인인증 이메일을 발송합니다.")
    fun authEmailSent(
        @RequestParam email: String,
        @RequestParam userId: Long,
    ): Response<String> = Response.ok(userService.sendVerificationEmail(email, userId))

    @PostMapping("/auth/email/verify")
    @Operation(summary = "본인인증 이메일 코드 검증", description = "이메일로 발송한 본인인증 코드를 확인합니다.")
    fun authEmailVerify(
        @RequestParam email: String,
        @RequestParam emailCode: String,
        @RequestParam userId: Long,
    ): Response<Result> = Response.ok(userService.verifyEmail(userId, email, emailCode))
}
