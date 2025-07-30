package com.sendy.application.usecase.auth

import com.sendy.application.dto.auth.DeviceInfoDto
import com.sendy.application.usecase.auth.interfaces.VerifyUserCredentials
import com.sendy.application.usecase.auth.interfaces.*
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val verifyUserCredentials: VerifyUserCredentials,
    private val invalidateUserTokens: InvalidateUserTokens,
    private val updateUserActivity: UpdateUserActivity,
    private val issueTokenUseCase: IssueTokenUseCase,
): LoginServiceUseCase {

    override fun login(command: LoginCommand): LoginResult {
        // 1. 사용자 인증
        val user = verifyUserCredentials.execute(command.dto.email, command.dto.password)
        
        // 2. 기존 토큰 무효화
        invalidateUserTokens.execute(user.id)
        
        // 3. 사용자 활동 시간 업데이트
        updateUserActivity.execute(user)
        
        // 4. 새 토큰 발급
        val deviceInfo = command.dto.deviceInfo ?: DeviceInfoDto()
        val tokenResponse = issueTokenUseCase.execute(user.id, deviceInfo, command.request)
        
        return LoginResult(tokenResponse = tokenResponse)
    }
}
