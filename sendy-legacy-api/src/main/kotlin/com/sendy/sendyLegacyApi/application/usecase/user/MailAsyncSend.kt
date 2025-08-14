package com.sendy.sendyLegacyApi.application.usecase.user

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class MailAsyncSend {
    @Async
    fun sendEmailAsync(mailSender: JavaMailSender, email: String, code: String): Boolean {
        try{
            val message = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")
            helper.setTo(email)
            helper.setSubject("회원가입 인증 코드")
            helper.setText("인증 코드: $code", true)
            helper.setFrom("sendy.smtp.test@gmail.com")
            mailSender.send(message)

        }catch (e: Exception) {
            return false;
            e.printStackTrace()
        }
        return true;
    }
}