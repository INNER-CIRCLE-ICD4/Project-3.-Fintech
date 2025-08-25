package com.sendy.sendyLegacyApi.application.usecase.authorities

import com.sendy.sendyLegacyApi.application.dto.authorities.TokenResponseDto
import com.sendy.sendyLegacyApi.application.dto.login.DeviceInfoDto
import com.sendy.sendyLegacyApi.application.dto.login.LoginRequestDto
import com.sendy.sendyLegacyApi.domain.authorities.UserEntityRepository
import com.sendy.sendyLegacyApi.domain.user.UserEntity
import com.sendy.sendyLegacyApi.support.error.ErrorCode
import com.sendy.sendyLegacyApi.support.exception.ServiceException
import com.sendy.sendyLegacyApi.support.util.SHA256Util
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val sha256Util: SHA256Util,
    private val jwtTokenStorageService: JwtTokenStorageService,
    private val userRepository: UserEntityRepository,
    private val deviceService: DeviceService,
    private val tokenService: TokenService
)  {
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun login(dto: LoginRequestDto,request: HttpServletRequest): TokenResponseDto {

        // 1. 사용자 인증
        val userEntity = VerifiedUser(dto.id, dto.password)

        // 2. 사용자의 기존 토큰 무효화
        //⚠️ 단일 디바이스 로그인 정책: 기존 모든 토큰을 PENDING_LOGOUT 상태로 변경
        logger.info("사용자 ID ${userEntity.id}의 기존 모든 토큰을 PENDING_LOGOUT 상태로 변경합니다")
        jwtTokenStorageService.setPendingLogoutByUserId(userEntity.id)

        // 3. 사용자 활동 시간 업데이트
        userEntity.updateLastActivity()

        // 4. 새 토큰 발급
        val deviceInfo = dto.deviceInfo ?: DeviceInfoDto()
//        val tokenResponse = issueTokenUseCase.execute(userEntity.id, deviceInfo, request)

        // 새 디바이스 정보 저장/업데이트
        val device = deviceService.saveOrUpdateDevice(userEntity.id, deviceInfo, request)

        // 새 토큰 발급 (디바이스 ID 포함)
        logger.info("user id :  ${userEntity.id}, device ID : ${device.id} == new token OK")
        val newToken =  tokenService.issueToken(userEntity.id, device.id)

        return newToken

    }

    fun VerifiedUser(
        id: Long,
        password: String
    ): UserEntity {
        val userEntity = userRepository.findActiveById(id)
            ?: throw ServiceException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다")

        // 도메인 로직을 통한 로그인 가능 여부 확인
        if (!userEntity.canLogin()) {
            throw ServiceException(ErrorCode.INVALID_INPUT_VALUE, "로그인할 수 없는 사용자입니다")
        }
        // SHA-256 해시로 비밀번호 검증
        if (!sha256Util.matches(password, userEntity.password)) {
            throw ServiceException(ErrorCode.INVALID_INPUT_VALUE, "비밀번호가 일치하지 않습니다")
        }
        return userEntity
    }
}
