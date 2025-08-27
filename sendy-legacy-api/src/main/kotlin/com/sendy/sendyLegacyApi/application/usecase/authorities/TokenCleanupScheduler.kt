package com.sendy.sendyLegacyApi.application.usecase.authorities

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * JWT 토큰 정리를 위한 스케줄러
 */
@Component
class TokenCleanupScheduler(
    private val jwtTokenStorageService: JwtTokenStorageService,
) {
    private val logger = LoggerFactory.getLogger(TokenCleanupScheduler::class.java)

    /**
     * 매일 새벽 2시에 만료된 토큰과 REVOKED 토큰들을 정리
     * cron: 초 분 시 일 월 요일
     */
    @Scheduled(cron = "0 0 2 * * ?")
    fun cleanupTokensDaily() {
        try {
            logger.info("토큰 정리 작업을 시작합니다...")

            val startTime = System.currentTimeMillis()
            jwtTokenStorageService.cleanupAllTokens()
            val endTime = System.currentTimeMillis()

            logger.info("토큰 정리 작업이 완료되었습니다. 소요시간: ${endTime - startTime}ms")
        } catch (e: Exception) {
            logger.error("토큰 정리 작업 중 오류가 발생했습니다: ${e.message}", e)
        }
    }

    /**
     * 매시간 REVOKED 토큰들만 정리 (빠른 정리)
     */
    @Scheduled(cron = "0 0 * * * ?")
    fun cleanupRevokedTokensHourly() {
        try {
            logger.debug("REVOKED 토큰 정리 작업을 시작합니다...")

            val startTime = System.currentTimeMillis()
            jwtTokenStorageService.cleanupRevokedTokens()
            val endTime = System.currentTimeMillis()

            logger.debug("REVOKED 토큰 정리 작업이 완료되었습니다. 소요시간: ${endTime - startTime}ms")
        } catch (e: Exception) {
            logger.error("REVOKED 토큰 정리 작업 중 오류가 발생했습니다: ${e.message}", e)
        }
    }

    /**
     * 1주일 이전의 REVOKED 토큰들을 정리 (주간 정리)
     */
    @Scheduled(cron = "0 0 3 * * 1") // 매주 월요일 새벽 3시
    fun cleanupOldRevokedTokensWeekly() {
        try {
            logger.info("오래된 REVOKED 토큰 정리 작업을 시작합니다...")

            val oneWeekAgo = LocalDateTime.now().minusWeeks(1)
            val startTime = System.currentTimeMillis()
            jwtTokenStorageService.cleanupRevokedTokensBefore(oneWeekAgo)
            val endTime = System.currentTimeMillis()

            logger.info("오래된 REVOKED 토큰 정리 작업이 완료되었습니다. 소요시간: ${endTime - startTime}ms")
        } catch (e: Exception) {
            logger.error("오래된 REVOKED 토큰 정리 작업 중 오류가 발생했습니다: ${e.message}", e)
        }
    }
} 
