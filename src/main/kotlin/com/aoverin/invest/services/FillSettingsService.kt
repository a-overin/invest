package com.aoverin.invest.services

import com.aoverin.invest.models.FillResult
import com.aoverin.invest.models.FillerType
import com.aoverin.invest.models.Stock
import java.time.LocalDate
import java.time.Period

interface FillSettingsService {

    fun getDatesToFill(stock: Stock, filler: FillerType, interval: Period): List<LocalDate>

    fun saveResultToLog(stock: Stock, filler: FillerType, fillDate: LocalDate, result: FillResult)
}