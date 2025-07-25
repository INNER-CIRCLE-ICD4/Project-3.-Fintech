package com.sendy

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.redis.core.RedisTemplate
import java.time.Duration

@SpringBootApplication
class SendyApplication(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    companion object {
        private const val EXPIRE_TIME = 2L
    }

    @PostConstruct
    fun init() {
        // TODO: String 뿐만 아니라 객체 타입도 가능한지 테스트
        val key = "accessToken:tokenValue"
        redisTemplate.opsForSet().add(key, "value")
        redisTemplate.expire(key, Duration.ofSeconds(EXPIRE_TIME)) // token expiredAt -> TTL

        Thread.sleep(Duration.ofSeconds(EXPIRE_TIME))
        println("redis hasKey: ${redisTemplate.hasKey(key)}") // false
    }
}

fun main(args: Array<String>) {
    runApplication<SendyApplication>(*args)
}
