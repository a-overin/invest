package com.aoverin.invest.dao.impl

import com.aoverin.invest.dao.StockDao
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class StockDaoImplTest {

    @Autowired
    private lateinit var stockDao: StockDao

    @Test
    fun getAllStocks() {
        println(stockDao.getAllStocks())
    }
}