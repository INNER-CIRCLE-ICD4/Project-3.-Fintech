package com.sendy.domain.service

import com.common.crypto.SHA256Util
import com.common.domain.error.ErrorCode
import com.common.domain.exceptions.ApiException
import com.sendy.application.dto.DeviceInfoDto
import com.sendy.domain.repository.DeviceInfoRepository
import com.sendy.domain.repository.UserEntityRepository
import com.sendy.infrastructure.persistence.DeviceInfoEntity
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * 디바이스 관리를 담당하는 서비스
 */
@Service
@Transactional
class DeviceService(
    private val deviceInfoRepository: DeviceInfoRepository,
    private val userEntityRepository: UserEntityRepository,
    private val sha256Util: SHA256Util
) {
    
    /**
     * 디바이스 정보를 기반으로 디바이스 핑거프린트 생성
     */
    fun generateDeviceFingerprint(
        userId: Long,
        deviceInfo: DeviceInfoDto?,
        request: HttpServletRequest
    ): String {
        val userAgent = deviceInfo?.userAgent ?: request.getHeader("User-Agent") ?: ""
        // 클라이언트에서 제공한 IP가 있으면 우선 사용, 없으면 서버에서 추출
        val ipAddress = deviceInfo?.ipAddress ?: getClientIpAddress(request)
        val acceptLanguage = request.getHeader("Accept-Language") ?: ""
        
        // 디바이스 핑거프린트 생성을 위한 요소들
        val fingerprintData = buildString {
            append(userId)
            append("|")
            append(userAgent)
            append("|")
            append(ipAddress)
            append("|")
            append(deviceInfo?.screenResolution ?: "")
            append("|")
            append(deviceInfo?.timezone ?: "")
            append("|")
            append(deviceInfo?.language ?: acceptLanguage)
            append("|")
            append(deviceInfo?.isMobile ?: false)
        }
        
        return sha256Util.createFixedHash(fingerprintData)
    }
    
    /**
     * 디바이스 정보 저장 또는 업데이트
     */
    fun saveOrUpdateDevice(
        userId: Long,
        deviceInfo: DeviceInfoDto?,
        request: HttpServletRequest
    ): DeviceInfoEntity {
        // UserEntity 조회
        val user = userEntityRepository.findById(userId)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다") }
            
        val fingerprint = generateDeviceFingerprint(userId, deviceInfo, request)
        val existingDevice = deviceInfoRepository.findByDeviceFingerprint(fingerprint)
        
        return if (existingDevice.isPresent) {
            // 기존 디바이스 정보 업데이트
            val device = existingDevice.get()
            device.lastLoginAt = LocalDateTime.now()
            device.updatedAt = LocalDateTime.now()
            deviceInfoRepository.save(device)
        } else {
            // 새 디바이스 정보 저장
            // 클라이언트에서 제공한 IP가 있으면 우선 사용, 없으면 서버에서 추출
            val finalIpAddress = deviceInfo?.ipAddress ?: getClientIpAddress(request)
            
            val newDevice = DeviceInfoEntity(
                user = user,
                deviceFingerprint = fingerprint,
                deviceName = deviceInfo?.deviceName,
                userAgent = deviceInfo?.userAgent ?: request.getHeader("User-Agent"),
                ipAddress = finalIpAddress,
                browserInfo = extractBrowserInfo(request.getHeader("User-Agent")),
                osInfo = extractOSInfo(request.getHeader("User-Agent")),
                screenResolution = deviceInfo?.screenResolution,
                timezone = deviceInfo?.timezone,
                language = deviceInfo?.language ?: request.getHeader("Accept-Language"),
                isMobile = deviceInfo?.isMobile ?: false,
                lastLoginAt = LocalDateTime.now()
            )
            deviceInfoRepository.save(newDevice)
        }
    }
    
    /**
     * 사용자의 모든 디바이스 조회
     */
    @Transactional(readOnly = true)
    fun getUserDevices(userId: Long): List<DeviceInfoEntity> {
        return deviceInfoRepository.findByUserId(userId)
    }
    
    /**
     * 클라이언트 IP 주소 추출 (프록시, 로드밸런서 고려)
     */
    private fun getClientIpAddress(request: HttpServletRequest): String {
        val xForwardedFor = request.getHeader("X-Forwarded-For")
        val xRealIP = request.getHeader("X-Real-IP")
        val xForwardedProto = request.getHeader("X-Forwarded-Proto")
        val cfConnectingIP = request.getHeader("CF-Connecting-IP") // Cloudflare
        val xClientIP = request.getHeader("X-Client-IP")
        
        return when {
            !cfConnectingIP.isNullOrBlank() -> cfConnectingIP
            !xForwardedFor.isNullOrBlank() -> xForwardedFor.split(",")[0].trim()
            !xRealIP.isNullOrBlank() -> xRealIP
            !xClientIP.isNullOrBlank() -> xClientIP
            else -> request.remoteAddr
        }
    }
    
    /**
     * User-Agent에서 브라우저 정보 추출 (버전 포함)
     */
    private fun extractBrowserInfo(userAgent: String?): String? {
        if (userAgent.isNullOrBlank()) return null
        
        val ua = userAgent.lowercase()
        return when {
            ua.contains("chrome") -> {
                val version = extractVersion(userAgent, "chrome/")
                "Chrome $version"
            }
            ua.contains("firefox") -> {
                val version = extractVersion(userAgent, "firefox/")
                "Firefox $version"
            }
            ua.contains("safari") && !ua.contains("chrome") -> {
                val version = extractVersion(userAgent, "version/")
                "Safari $version"
            }
            ua.contains("edge") -> {
                val version = extractVersion(userAgent, "edge/")
                "Edge $version"
            }
            ua.contains("opera") -> {
                val version = extractVersion(userAgent, "opera/")
                "Opera $version"
            }
            else -> "Unknown Browser"
        }
    }
    
    /**
     * User-Agent에서 버전 정보 추출
     */
    private fun extractVersion(userAgent: String, prefix: String): String {
        val startIndex = userAgent.lowercase().indexOf(prefix)
        if (startIndex == -1) return ""
        
        val versionStart = startIndex + prefix.length
        val versionEnd = userAgent.indexOf(" ", versionStart)
        return if (versionEnd == -1) {
            userAgent.substring(versionStart)
        } else {
            userAgent.substring(versionStart, versionEnd)
        }
    }
    
    /**
     * User-Agent에서 OS 정보 추출 (버전 포함)
     */
    private fun extractOSInfo(userAgent: String?): String? {
        if (userAgent.isNullOrBlank()) return null
        
        val ua = userAgent.lowercase()
        return when {
            ua.contains("windows") -> {
                val version = when {
                    ua.contains("windows nt 10.0") -> "10"
                    ua.contains("windows nt 6.3") -> "8.1"
                    ua.contains("windows nt 6.2") -> "8"
                    ua.contains("windows nt 6.1") -> "7"
                    ua.contains("windows nt 6.0") -> "Vista"
                    ua.contains("windows nt 5.2") -> "XP x64"
                    ua.contains("windows nt 5.1") -> "XP"
                    else -> ""
                }
                "Windows $version".trim()
            }
            ua.contains("mac os") -> {
                val version = extractVersion(userAgent, "mac os ")
                "macOS $version"
            }
            ua.contains("linux") -> {
                when {
                    ua.contains("ubuntu") -> "Ubuntu"
                    ua.contains("fedora") -> "Fedora"
                    ua.contains("centos") -> "CentOS"
                    ua.contains("debian") -> "Debian"
                    else -> "Linux"
                }
            }
            ua.contains("android") -> {
                val version = extractVersion(userAgent, "android ")
                "Android $version"
            }
            ua.contains("iphone") || ua.contains("ipad") -> {
                val version = extractVersion(userAgent, "os ")
                "iOS $version"
            }
            else -> "Unknown OS"
        }
    }
    
    /**
     * 문자열을 SHA-256으로 해시 (고정된 해시)
     * 디바이스 핑거프린트용 - 같은 입력에 대해 항상 같은 결과 보장
     */
    private fun createFingerprint(input: String): String {
        return sha256Util.createFixedHash(input)
    }
} 