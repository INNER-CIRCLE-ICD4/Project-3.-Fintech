package com.sendy.infrastructure.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI().info(
            Info()
                .title("API 타이틀")
                .description("API 설명입니다.")
                .version("1.0.0"),
        )
}
