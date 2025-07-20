package com.sendy.domain.model

import java.time.LocalDateTime

/**
 * pure domain model (JPA 엔티티가 아님)
 * 사용자 도메인의 핵심 비즈니스 로직을 캡슐화
 */
data class User(
    val id: Long = 0,
    val email: String,
    val password: String,
    val name: String,
    val phoneNumber: String,
    val isDelete: Boolean = false,
    val emailVerified: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    
    /**
     * 사용자가 삭제되었는지 확인
     */
    fun isDeleted(): Boolean = isDelete
    
    /**
     * 사용자가 로그인 가능한 상태인지 확인
     */
    fun canLogin(): Boolean = !isDelete && emailVerified
    
    /**
     * 사용자가 이메일 인증이 완료되었는지 확인
     */
    fun isEmailVerified(): Boolean = emailVerified
    
    /**
     * 사용자 정보 업데이트
     */
    fun updateInfo(
        name: String? = null,
        phoneNumber: String? = null
    ): User = copy(
        name = name ?: this.name,
        phoneNumber = phoneNumber ?: this.phoneNumber,
        updatedAt = LocalDateTime.now()
    )
    
    /**
     * 이메일 인증 완료 처리
     */
    fun verifyEmail(): User = copy(
        emailVerified = true,
        updatedAt = LocalDateTime.now()
    )
    
    /**
     * 사용자 삭제 처리 (소프트 삭제)
     */
    fun delete(): User = copy(
        isDelete = true,
        updatedAt = LocalDateTime.now()
    )
    
    /**
     * 비밀번호 검증
     */
    fun validatePassword(inputPassword: String): Boolean = password == inputPassword
    
    /**
     * 사용자 활성화 상태 확인
     */
    fun isActive(): Boolean = !isDelete
    
    /**
     * 마지막 업데이트 시간 갱신
     */
    fun updateLastActivity(): User = copy(
        updatedAt = LocalDateTime.now()
    )
}