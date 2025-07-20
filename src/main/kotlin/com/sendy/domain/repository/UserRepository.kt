package com.sendy.domain.repository

import com.sendy.domain.model.User
import java.util.*

/**
 * 도메인 계층에서 사용하는 User 리포지토리 인터페이스
 * 인프라스트럭처 계층의 UserEntityRepository와 분리
 */
interface UserRepository {
    
    /**
     * ID로 사용자 조회
     */
    fun findById(id: Long): Optional<User>
    
    /**
     * 이메일로 사용자 조회
     */
    fun findByEmail(email: String): Optional<User>
    
    /**
     * 전화번호로 사용자 조회
     */
    fun findByPhoneNumber(phoneNumber: String): Optional<User>
    
    /**
     * 활성 사용자 ID로 조회
     */
    fun findActiveById(id: Long): Optional<User>
    
    /**
     * 활성 사용자 이메일로 조회
     */
    fun findActiveByEmail(email: String): Optional<User>
    
    /**
     * 활성 사용자 전화번호로 조회
     */
    fun findActiveByPhoneNumber(phoneNumber: String): Optional<User>
    
    /**
     * 사용자 저장
     */
    fun save(user: User): User
    
    /**
     * 사용자 존재 여부 확인
     */
    fun existsByEmail(email: String): Boolean
    
    /**
     * 전화번호 존재 여부 확인
     */
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    
    /**
     * 활성 사용자 이메일 존재 여부 확인
     */
    fun existsActiveByEmail(email: String): Boolean
    
    /**
     * 활성 사용자 전화번호 존재 여부 확인
     */
    fun existsActiveByPhoneNumber(phoneNumber: String): Boolean
} 