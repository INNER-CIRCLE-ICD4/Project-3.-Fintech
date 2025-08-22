package com.sendy.support.util


import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*


@Component
class LogUtilInterceptor : HandlerInterceptor {
    private val logger = org.slf4j.LoggerFactory.getLogger(LogUtilInterceptor::class.java)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(handler is HandlerMethod) {
            val method = handler.method
            val handlerName = handler.beanType.simpleName
            val methodName = handler.method.name
            val controllerInfo = "$handlerName.$methodName"

            val basePath = request.servletPath.split("/").getOrNull(1) ?: "etc"

            val traceId = UUID.randomUUID().toString()
            MDC.put("traceId", traceId)
            MDC.put("basePath", basePath)
            MDC.put("handler", controllerInfo)
            logger.info("PreHandle method called. Request URI: ${request.requestURI}, Method: ${request.method}, Handler: $controllerInfo")
        }
        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        logger.info("** After Completion method ** ")
        MDC.clear()
    }

}