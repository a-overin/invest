package com.aoverin.invest.services.impl

import com.aoverin.invest.configurations.InfoFillConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class PolygonApiTest {

    @Autowired
    private lateinit var polygonApi: PolygonApi

    @Autowired
    private lateinit var conf: InfoFillConfiguration

    @Test
    fun getStockInfoByDateAndCode() {
//        println(polygonApi.getStockInfoByDateAndCode("QIWI", LocalDate.of(2022, 1, 4)))
//        println(polygonApi.getStockCostByDateAndCode("QIWI", LocalDate.of(2022, 1, 4)))
//        println(conf.period.months)
//        println(conf.period.days)
//        println(LocalDate.of(2021, 12, 1).rangeTo(LocalDate.now()))
//        println(LocalDate.of(2021, 12, 1).datesUntil(LocalDate.now(), conf.period).toList())
//        println(polygonApi.getStockInfoByDateAndCode("ATVI", LocalDate.of(2022, 2, 1)))
//        println(polygonApi.getStockCostByDateAndCode("QIWI", LocalDate.of(2022, 2, 25)))
    }
}