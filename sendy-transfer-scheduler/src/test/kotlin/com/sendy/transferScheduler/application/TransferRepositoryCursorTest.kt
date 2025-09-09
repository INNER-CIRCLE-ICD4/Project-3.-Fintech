//package com.sendy.transferScheduler.application
//
//import com.sendy.transferDomain.domain.ReservationTransfer
//import com.sendy.transferDomain.domain.TransferRepository
//import com.sendy.transferScheduler.config.TestJdbcConfig
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.jdbc.core.JdbcTemplate
//import org.springframework.jdbc.core.simple.JdbcClient
//import org.springframework.test.context.junit.jupiter.SpringExtension
//import java.time.LocalDateTime
//import java.time.Month
//import kotlin.test.assertEquals
//
//@ExtendWith(SpringExtension::class)
//@SpringBootTest(
//    classes = [TestJdbcConfig::class],
//    properties = ["spring.config.location=classpath:application.yml"],
//    webEnvironment = SpringBootTest.WebEnvironment.NONE,
//)
//class TransferRepositoryCursorTest(
//    @Autowired
//    private val transferRepository: TransferRepository,
//    @Autowired
//    private val jdbcTemplate: JdbcTemplate,
//) {
//    val jdbcClient = JdbcClient.create(jdbcTemplate)
//
//    companion object {
//        private const val MAX_SIZE = 30
//        private const val FETCH_SIZE = 10
//    }
//
//    @BeforeEach
//    fun setUp() {
//        val range = (0..<MAX_SIZE)
//        val preparedStmt = range.joinToString(",\n") { "(?, ?, ?, ?, ?, ?)" }
//
//        val sqlQb =
//            jdbcClient
//                .sql(
//                    """
//                    insert into transfer
//                    (id, send_user_id, send_account_number, amount, status, scheduled_at)
//                    values
//                    $preparedStmt
//                    ;
//                    """.trimIndent(),
//                )
//
//        range.forEach {
//            val id = it + 1
//            val userId = id + 1_000
//            val sendAccountNumber = "321000000000${id % 10}"
//            sqlQb.params(id, userId, sendAccountNumber, 1_000, "RESERVE", LocalDateTime.of(2025, Month.JANUARY, 1, 0, 0, 0))
//        }
//
//        sqlQb.update()
//    }
//
//    @AfterEach
//    fun tearDown() {
//        jdbcClient.sql("truncate table transfer").update()
//    }
//
//    @Test
//    fun `cursor 방식으로 fetchSize 만큼 목록을 조회할 수 있어야된다`() {
//        // given
//        val expectList = mutableListOf<ReservationTransfer>()
//        val startDt = LocalDateTime.of(2025, Month.JANUARY, 1, 0, 0, 0).toString()
//        val endDt = LocalDateTime.of(2025, Month.JANUARY, 1, 1, 0, 0).toString()
//        val initExecute = transferRepository.getReservedTransferByCursor(startDt, endDt, fetchSize = FETCH_SIZE)
//        var nextCursor = initExecute.nextCursor
//        expectList.add(initExecute)
//
//        // when
//        while (nextCursor != null) {
//            val next = transferRepository.getReservedTransferByCursor(startDt, endDt, fetchSize = FETCH_SIZE, id = nextCursor.id)
//            nextCursor = next.nextCursor
//            if (next.nextCursor != null) {
//                expectList.add(initExecute)
//            }
//        }
//
//        // then
//        assertEquals(initExecute.totalCount, MAX_SIZE)
//        assertEquals(expectList.size, MAX_SIZE / FETCH_SIZE)
//    }
//}
