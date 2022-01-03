package com.aoverin.invest.dao

import java.time.LocalDate

interface LogFillDao {

    fun getFilledDates(stockCode: String, fillerTypeName: String): List<LocalDate>

    fun saveFillResult(stockCode:String, fillerTypeName: String, fillDate: LocalDate, result: String)
}