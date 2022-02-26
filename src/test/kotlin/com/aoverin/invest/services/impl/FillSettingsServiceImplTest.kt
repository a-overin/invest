package com.aoverin.invest.services.impl

import com.aoverin.invest.models.FillerType
import com.aoverin.invest.models.Stock
import com.aoverin.invest.services.FillSettingsService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.Period

@SpringBootTest
internal class FillSettingsServiceImplTest {
    @Autowired
    lateinit var service: FillSettingsService

    @Test
    fun testDates() {
        val s = Stock(code = "QIWI", info = null, startFill = LocalDate.of(2021, 1, 1))
        println(service.getDatesToFill(s, FillerType.STOCK_COSTS, Period.ofDays(1)))
    }
}