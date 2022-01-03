package com.aoverin.invest.dao.impl

import com.aoverin.invest.dao.LogFillDao
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class LogFillDaoImplTest {

    @Autowired
    private lateinit var dao: LogFillDao

    @Test
    fun getAllLogs() {
        println(dao.getFilledDates("one", "one"))
    }
}