package com.sendy.transferScheduler

import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals

class DateTest {
    @Test
    fun start() {
        val fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault())
        val now = LocalDateTime.now(fixed)
        val truncatedTo = now.truncatedTo(ChronoUnit.HOURS)
        val startDt = "${truncatedTo.minus(1, ChronoUnit.HOURS)}:00"
        val endDt = "$truncatedTo:00"

        assertEquals(
            startDt.split("T")[1],
            LocalDateTime
                .now(fixed)
                .toLocalTime()
                .minus(1, ChronoUnit.HOURS)
                .truncatedTo(ChronoUnit.HOURS)
                .toString() + ":00",
        )
        assertEquals(
            endDt.split("T")[1],
            LocalDateTime
                .now(fixed)
                .toLocalTime()
                .truncatedTo(ChronoUnit.HOURS)
                .toString() + ":00",
        )
    }
}
