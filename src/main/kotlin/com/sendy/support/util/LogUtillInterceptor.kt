package com.sendy.support.util

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor


@Component
class LogUtillInterceptor : HandlerInterceptor {
    val log = org.slf4j.LoggerFactory.getLogger(this.javaClass)
    override fun preHandle(
        request: HttpServletRequest, response: HttpServletResponse, handler: Any
    ): Boolean {
        if (handler is HandlerMethod) {
            val requestMapping = handler.beanType.getAnnotation(RequestMapping::class.java)

            requestMapping?.value?.firstOrNull()?.let { path ->
                val apipath = path.removePrefix("/").split("/").first()
                MDC.put("apipath", apipath)
            }
        }
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        // 요청 끝나면 제거 (메모리 누수 방지)
        MDC.remove("apipath")
    }
}