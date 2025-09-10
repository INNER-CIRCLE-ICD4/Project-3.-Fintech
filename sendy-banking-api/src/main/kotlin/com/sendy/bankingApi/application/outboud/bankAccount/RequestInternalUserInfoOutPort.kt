package com.sendy.bankingApi.application.outboud.bankAccount

import com.sendy.bankingApi.domain.vo.UserId

interface RequestInternalUserInfoOutPort {
    fun requestExternalUserInfo(userId: UserId): RequestInternalUserInfoResponseDto

    data class RequestInternalUserInfoResponseDto(
        val userId: Long,
        val isDelete: Boolean,
    )
}
