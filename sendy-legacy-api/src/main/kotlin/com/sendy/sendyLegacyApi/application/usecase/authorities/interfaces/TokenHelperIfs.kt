package com.sendy.sendyLegacyApi.application.usecase.authorities.interfaces

import com.sendy.sendyLegacyApi.application.dto.authorities.TokenDto

interface TokenHelperIfs {
    fun issueAccessToken(data: Map<String, Any>): TokenDto

    fun issueRefreshToken(data: Map<String, Any>): TokenDto

    fun validationTokenWithThrow(token: String): Map<String, Any>
}
