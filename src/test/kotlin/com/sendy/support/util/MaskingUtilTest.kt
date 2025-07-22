package com.sendy.support.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MaskingUtilTest {
    @Test
    fun `이름 마스킹`() {
        assertEquals("홍*동", MaskingUtil.maskName("홍길동"))
        assertEquals("원*", MaskingUtil.maskName("원빈"))
        assertEquals("이", MaskingUtil.maskName("이"))
        assertEquals("김*", MaskingUtil.maskName("김민"))
        assertEquals("", MaskingUtil.maskName(""))
    }

    @Test
    fun `휴대폰 번호 마스킹`() {
        assertEquals("010-****-5678", MaskingUtil.maskPhone("010-1234-5678"))
        assertEquals("011-****-1234", MaskingUtil.maskPhone("011-9999-1234"))
        assertEquals("010-****-0000", MaskingUtil.maskPhone("010-1111-0000"))
        assertEquals("01012345678", MaskingUtil.maskPhone("01012345678"))
        assertEquals("", MaskingUtil.maskPhone(""))
    }

    @Test
    fun `계좌번호 마스킹`() {
        assertEquals("**********1234", MaskingUtil.maskAccount("12345678901234"))
        assertEquals("****5678", MaskingUtil.maskAccount("12345678"))
        assertEquals("**3478", MaskingUtil.maskAccount("123478"))
        assertEquals("1234", MaskingUtil.maskAccount("1234"))
        assertEquals("12", MaskingUtil.maskAccount("12"))
        assertEquals("*****6789", MaskingUtil.maskAccount("123456789"))
        assertEquals("******7890", MaskingUtil.maskAccount("1234567890"))
        assertEquals("*******8901", MaskingUtil.maskAccount("12345678901"))
    }
}
