package com.sendy.bankingApi.adapter.outbound.internal

import com.sendy.bankingApi.adapter.outbound.internal.feign.UserApiCaller
import com.sendy.bankingApi.application.outboud.bankAccount.RequestInternalUserInfoOutPort
import com.sendy.bankingApi.domain.vo.UserId
import org.springframework.stereotype.Component

@Component
class UserInternalAdapter(
    private val userApiCaller: UserApiCaller,
) : RequestInternalUserInfoOutPort {
    override fun requestExternalUserInfo(userId: UserId): RequestInternalUserInfoOutPort.RequestInternalUserInfoResponseDto =
        try {
            userApiCaller.callGetUser(userId.value)
        } catch (e: RuntimeException) {
            throw RuntimeException("user api http error")
        }
}
