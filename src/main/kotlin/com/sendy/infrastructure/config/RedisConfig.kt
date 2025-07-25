package com.sendy.infrastructure.config

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableConfigurationProperties(value = [RedisConfig.RedisProperties::class])
class RedisConfig(
    private val redisProperties: RedisProperties,
) {
    private val logger = LoggerFactory.getLogger(RedisConfig::class.java)

    @ConfigurationProperties(prefix = "spring.data.redis")
    data class RedisProperties
        @ConstructorBinding
        constructor(
            val host: String,
            val port: Int,
            val password: String,
        )

    @Bean
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        logger.info(
            """
            Redis Info:
                Host: {},
                Port: {},
                Database: {}
            """.trimIndent(),
            redisProperties.host,
            redisProperties.port,
            redisProperties.password,
        )
        val redisConfiguration = RedisStandaloneConfiguration(redisProperties.host, redisProperties.port)

        redisConfiguration.database = 0
        redisConfiguration.setPassword(redisProperties.password)

        return LettuceConnectionFactory(
            redisConfiguration,
            LettuceClientConfiguration
                .builder()
                .commandTimeout(Duration.ofSeconds(2))
                .shutdownTimeout(Duration.ZERO)
                .build(),
        )
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val redisTemplate = RedisTemplate<String, String>()

        redisTemplate.connectionFactory = redisConnectionFactory
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer()
        redisTemplate.hashKeySerializer = StringRedisSerializer()

        return redisTemplate
    }
}
