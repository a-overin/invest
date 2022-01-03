package com.aoverin.invest.services

import com.aoverin.invest.models.StockCost
import com.aoverin.invest.models.StockInfo
import java.time.LocalDate

interface StockMarketApi {

    fun getStockCostByDateAndCode(code: String, date: LocalDate): StockCost?

    fun getStockInfoByDateAndCode(code: String, date: LocalDate): StockInfo?
}