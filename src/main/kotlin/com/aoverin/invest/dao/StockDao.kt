package com.aoverin.invest.dao

import com.aoverin.invest.models.Stock
import com.aoverin.invest.models.StockCost
import com.aoverin.invest.models.StockInfo
import java.time.LocalDate

interface StockDao {

    fun getAllStocks() : List<Stock>

    fun saveStockInfoByDate(stockInfo: StockInfo, date: LocalDate)

    fun saveStockCost(stockCost: StockCost)
}