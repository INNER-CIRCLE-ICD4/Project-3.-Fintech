package com.sendy.sendyLegacyApi.domain.auth.token.ifs

import com.sendy.sendyLegacyApi.domain.auth.token.model.TokenDto

interface TokenHelperIfs {
    fun issueAccessToken(data: Map<String, Any>): TokenDto

    fun issueRefreshToken(data: Map<String, Any>): TokenDto

    fun validationTokenWithThrow(token: String): Map<String, Any>
}
