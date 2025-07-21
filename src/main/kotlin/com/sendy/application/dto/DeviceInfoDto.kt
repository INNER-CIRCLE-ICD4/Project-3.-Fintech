package com.sendy.application.dto

/**
 * 클라이언트에서 전송하는 디바이스 정보
 */
data class DeviceInfoDto(
    val deviceName: String? = null,
    val userAgent: String? = null,
    val ipAddress: String? = null, // 클라이언트에서 수집한 공용 IP (선택적)
    val screenResolution: String? = null,
    val timezone: String? = null,
    val language: String? = null,
    val isMobile: Boolean = false
) 