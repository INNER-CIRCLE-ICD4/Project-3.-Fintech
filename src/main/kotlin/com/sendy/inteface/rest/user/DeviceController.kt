package com.sendy.inteface.rest.user

import com.sendy.domain.service.DeviceService
import com.sendy.infrastructure.persistence.DeviceInfoEntity
import com.sendy.inteface.rest.user.ui.Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@Tag(name = "Device", description = "디바이스 관리 API (단일 디바이스 정책)")
@RestController
@RequestMapping("/user/devices")
class DeviceController(
    private val deviceService: DeviceService
) {
    
    @Operation(
        summary = "현재 로그인된 디바이스 정보 조회",
        description = """
            단일 디바이스 정책에 따라 현재 로그인된 디바이스의 정보를 조회합니다.
            최대 1개의 디바이스 정보만 반환됩니다.
        """
    )
    @GetMapping
    fun getCurrentDevice(authentication: Authentication): Response<List<DeviceInfoEntity>> {
        val userId = authentication.name.toLong()
        val devices = deviceService.getUserDevices(userId)
        
        // 단일 디바이스 정책: 최대 1개의 디바이스만 존재
        return Response.ok(devices.take(1))
    }
    
    // 단일 디바이스 정책에서는 특정 디바이스 로그아웃이 불필요하므로 제거
    // 새 디바이스에서 로그인하면 자동으로 기존 디바이스가 로그아웃됨
} 