package com.sendy.inteface.rest.user

import com.sendy.application.dto.RegisterUserRequestDto
import com.sendy.application.dto.RegisterUserResponseDto
import com.sendy.application.dto.UpdateUserRequestDto
import com.sendy.domain.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 *
 * controller
 * 유저 등록하는 컨트롤러
 */
@Tag(name = "User", description = "회원 API")
@RestController
@RequestMapping("/user")
class RegisterUserController(
    var UserService: UserService,

    ) {

    @PostMapping("/register")
    @Operation(summary = "사용자 등록", description = "사용자 정보로 회원가입을 합니다.")
    fun registerUser(@RequestBody @Valid requestDto: RegisterUserRequestDto): ResponseEntity<RegisterUserResponseDto> {
        return ResponseEntity(UserService.registerUser(requestDto), HttpStatus.OK)
    }

    @PutMapping("/update")
    @Operation(summary = "사용자 수정", description = "사용자 정보를 수정합니다.")
    fun updateUser(@RequestBody @Valid updateDto: UpdateUserRequestDto, @RequestHeader("Authorization") token: String,): ResponseEntity<RegisterUserResponseDto> {
        //로그인된 사용자 엔티티 조회

        return ResponseEntity(UserService.updateUser(token,updateDto), HttpStatus.OK)
    }

    @PutMapping("/deete")
    @Operation(summary = "사용자 삭제", description = "사용자 정보를 삭제합니다.")
    fun deleteUser(email: String,password: String, @RequestHeader("Authorization") token: String,): ResponseEntity<String> {
        //로그인된 사용자 엔티티 조회

        return ResponseEntity(UserService.deleteUser(email,password,token), HttpStatus.OK)
    }

    @PostMapping("/auth/email/send")
    @Operation(summary = "본인인증 이메일 발송", description = "회원가입시 본인인증 이메일을 발송합니다.")
    fun authEmailSent(email : String): ResponseEntity<String> {
        return ResponseEntity(UserService.sendVerificationEmail(email), HttpStatus.OK)
    }

    @PostMapping("/auth/email/verify")
    @Operation(summary = "본인인증 이메일 코드 검증", description = "이메일로 발송한 본인인증 코드를 확인합니다.")
    fun authEmailVerify(email : String, emailCode:String): ResponseEntity<String> {
        return ResponseEntity(UserService.verifyEmail(email,emailCode), HttpStatus.OK)
    }


}