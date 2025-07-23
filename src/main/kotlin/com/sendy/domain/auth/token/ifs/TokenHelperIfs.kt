package com.sendy.domain.auth.token.ifs

import com.sendy.domain.auth.token.model.TokenDto

interface TokenHelperIfs {
    fun issueAccessToken(data: Map<String, Any>): TokenDto

    fun issueRefreshToken(data: Map<String, Any>): TokenDto

    fun validationTokenWithThrow(token: String): Map<String, Any>
}
