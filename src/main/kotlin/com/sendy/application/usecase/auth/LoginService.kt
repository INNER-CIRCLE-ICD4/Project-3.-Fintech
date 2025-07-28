package com.sendy.application.usecase.auth

import com.sendy.application.dto.auth.DeviceInfoDto
import com.sendy.application.usecase.auth.interfaces.AuthenticateUserUseCase
import com.sendy.application.usecase.auth.interfaces.*
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val invalidateExistingTokensUseCase: InvalidateExistingTokensUseCase,
    private val updateUserActivityUseCase: UpdateUserActivityUseCase,
    private val issueTokenUseCase: IssueTokenUseCase,
): LoginServiceUseCase {

    override fun login(command: LoginCommand): LoginResult {
        // 1. 사용자 인증
        val user = authenticateUserUseCase.execute(command.dto.email, command.dto.password)
        
        // 2. 기존 토큰 무효화
        invalidateExistingTokensUseCase.execute(user.id)
        
        // 3. 사용자 활동 시간 업데이트
        updateUserActivityUseCase.execute(user)
        
        // 4. 새 토큰 발급
        val deviceInfo = command.dto.deviceInfo ?: DeviceInfoDto()
        val tokenResponse = issueTokenUseCase.execute(user.id, deviceInfo, command.request)
        
        return LoginResult(tokenResponse = tokenResponse)
    }
}
