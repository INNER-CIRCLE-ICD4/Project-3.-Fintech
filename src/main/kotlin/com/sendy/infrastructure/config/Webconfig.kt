package com.sendy.infrastructure.config

import com.sendy.support.util.LogUtilInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class Webconfig(private val mdcLoggingInterceptor: LogUtilInterceptor) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(mdcLoggingInterceptor)
    }


}