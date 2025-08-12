package com.sendy.domain.auth.token.business

import com.sendy.domain.auth.token.ifs.TokenHelperIfs
import com.sendy.domain.auth.token.model.TokenDto
import com.sendy.domain.auth.token.service.JwtTokenStorageService
import com.sendy.domain.enum.TokenType
import com.sendy.support.error.ErrorCode
import com.sendy.support.error.TokenErrorCode
import com.sendy.support.exception.ServiceException
import org.springframework.stereotype.Service

@Service
class TokenBusiness(
    private val tokenHelperIfs: TokenHelperIfs,
    private val jwtTokenStorageService: JwtTokenStorageService,
) {
    fun issueAccessToken(userId: Long): TokenDto {
        val data = mapOf("userId" to userId.toString()) // String으로 변환
        val tokenDto = tokenHelperIfs.issueAccessToken(data)

        // 데이터베이스에 토큰 저장 (jti 사용)
        jwtTokenStorageService.saveToken(
            userId = userId,
            jti = tokenDto.jti,
            tokenType = TokenType.ACCESS,
            expiredAt = tokenDto.expiredAt,
        )

        return tokenDto
    }

    fun issueRefreshToken(userId: Long): TokenDto {
        val data = mapOf("userId" to userId.toString()) // String으로 변환
        val tokenDto = tokenHelperIfs.issueRefreshToken(data)

        // 레디스에에 리프레시토큰 저장 (jti 사용)
        jwtTokenStorageService.saveRefreshToken(
            userId = userId,
            jti = tokenDto.jti,
            tokenType = TokenType.REFRESH,
            expiredAt = tokenDto.expiredAt,
        )

        return tokenDto
    }

    fun issueAccessToken(
        userId: Long,
        deviceId: Long,
    ): TokenDto {
        val data = mapOf("userId" to userId.toString(), "deviceId" to deviceId.toString()) // String으로 변환
        val tokenDto = tokenHelperIfs.issueAccessToken(data)

        // 데이터베이스에 토큰 저장 (디바이스 ID 포함, jti 사용)
        jwtTokenStorageService.saveToken(
            userId = userId,
            deviceId = deviceId,
            jti = tokenDto.jti,
            tokenType = TokenType.ACCESS,
            expiredAt = tokenDto.expiredAt,
        )

        return tokenDto
    }

    fun issueRefreshToken(
        userId: Long,
        deviceId: Long,
    ): TokenDto {
        val data = mapOf("userId" to userId.toString(), "deviceId" to deviceId.toString()) // String으로 변환
        val tokenDto = tokenHelperIfs.issueRefreshToken(data)

        // 레디스에 리프레시토큰 저장 (디바이스 ID 포함, jti 사용)
        jwtTokenStorageService.saveRefreshToken(
            userId = userId,
            deviceId = deviceId,
            jti = tokenDto.jti,
            tokenType = TokenType.REFRESH,
            expiredAt = tokenDto.expiredAt,
        )

        return tokenDto
    }

    fun validationToken(token: String): Long {
        // JWT 자체 검증 (서명, 만료시간 등)
        val claims = tokenHelperIfs.validationTokenWithThrow(token)
        val userIdStr = claims["userId"] as? String ?: throw ServiceException(ErrorCode.NULL_POINT)

        // JWT에서 jti 추출
        val jti = claims["jti"] ?: throw ServiceException(TokenErrorCode.INVALID_TOKEN, "토큰에 고유 식별자가 없습니다.")

        // userId를 Long으로 변환
        val userId = userIdStr.toLongOrNull() ?: throw ServiceException(TokenErrorCode.INVALID_TOKEN, "유효하지 않은 사용자 ID입니다.")

        // 데이터베이스에서 토큰 유효성 확인 (jti 사용)
        if (!jwtTokenStorageService.isTokenValid(jti.toString())) {
            throw ServiceException(TokenErrorCode.INVALID_TOKEN, "토큰이 무효화되었거나 존재하지 않습니다.")
        }

        return userId
    }

    /**
     * Refresh Token을 검증하고 새로운 Access Token을 발급합니다.
     * @param refreshToken 검증할 refresh token
     * @return 새로운 Access Token
     * @throws ServiceException refresh token이 유효하지 않거나 만료된 경우
     */
    fun refreshAccessToken(refreshToken: String): TokenDto {
        try {
            // Refresh Token JWT 자체 검증 (서명, 만료시간 등)
            val claims = tokenHelperIfs.validationTokenWithThrow(refreshToken)
            val userIdStr = claims["userId"] as? String ?: throw ServiceException(ErrorCode.NULL_POINT)

            // JWT에서 jti 추출
            val jti = claims["jti"] ?: throw ServiceException(TokenErrorCode.INVALID_TOKEN, "토큰에 고유 식별자가 없습니다.")

            // userId를 Long으로 변환
            val userId = userIdStr.toLongOrNull() ?: throw ServiceException(TokenErrorCode.INVALID_TOKEN, "유효하지 않은 사용자 ID입니다.")

            // 데이터베이스에서 Refresh Token 유효성 확인 (jti 사용)
            if (!jwtTokenStorageService.isTokenValid(jti.toString())) {
                throw ServiceException(TokenErrorCode.INVALID_TOKEN, "Refresh Token이 무효화되었거나 존재하지 않습니다.")
            }

            // 새로운 Access Token 발급
            return issueAccessToken(userId)
        } catch (e: ServiceException) {
            // Token 관련 예외는 그대로 전파
            when (e.getErrorCodeIfs()) {
                TokenErrorCode.EXPIRED_TOKEN -> throw ServiceException(TokenErrorCode.EXPIRED_TOKEN, "Refresh Token이 만료되었습니다. 다시 로그인해주세요.")
                TokenErrorCode.INVALID_TOKEN -> throw ServiceException(TokenErrorCode.INVALID_TOKEN, "유효하지 않은 Refresh Token입니다.")
                else -> throw ServiceException(TokenErrorCode.TOKEN_EXCEPTION, "Refresh Token 처리 중 오류가 발생했습니다.")
            }
        }
    }
}
