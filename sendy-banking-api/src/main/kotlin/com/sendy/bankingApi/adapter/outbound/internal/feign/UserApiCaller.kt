package com.sendy.bankingApi.adapter.outbound.internal.feign

import com.sendy.bankingApi.application.outboud.bankAccount.RequestInternalUserInfoOutPort
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "user-internal-api", url = "http://localhost:8080/internal/users")
interface UserApiCaller {
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun callGetUser(userId: Long): RequestInternalUserInfoOutPort.RequestInternalUserInfoResponseDto
}
