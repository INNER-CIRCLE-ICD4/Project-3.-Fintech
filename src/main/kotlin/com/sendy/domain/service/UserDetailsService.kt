package com.sendy.domain.service

import com.sendy.domain.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomerUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("해당 사용자가 없습니다: $username")
        return org.springframework.security.core.userdetails.User(
            user.email, user.password, listOf() // 권한 등 추가 필요시 여기서 설정
        )
    }
}