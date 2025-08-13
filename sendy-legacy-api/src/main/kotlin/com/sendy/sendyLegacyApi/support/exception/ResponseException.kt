package com.sendy.sendyLegacyApi.support.exception

import org.springframework.http.HttpStatus

class ResponseException(
    msg: String,
    val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
) : RuntimeException(msg)
