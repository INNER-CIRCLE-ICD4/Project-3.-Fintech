package com.sendy.support

import org.springframework.http.HttpStatus

class ResponseException : RuntimeException {
    val status: HttpStatus

    constructor(msg: String, status: HttpStatus) : super(msg) {
        this.status = status
    }

    constructor(status: HttpStatus) : super() {
        this.status = status
    }
}