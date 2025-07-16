package com.common.domain.exceptions

import com.common.domain.error.ErrorCode
import com.sendy.inteface.rest.user.ui.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<Response<Nothing>> {
        return ResponseEntity
            .status(e.errorCode.code)
            .body(Response.error(e.errorCode, e.message))
    }
}