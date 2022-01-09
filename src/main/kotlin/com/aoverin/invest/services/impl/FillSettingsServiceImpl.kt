package com.aoverin.invest.services.impl

import com.aoverin.invest.dao.LogFillDao
import com.aoverin.invest.models.FillResult
import com.aoverin.invest.models.FillerType
import com.aoverin.invest.models.Stock
import com.aoverin.invest.services.FillSettingsService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period
import kotlin.streams.toList

@Service
class FillSettingsServiceImpl(
    private val dao: LogFillDao
) : FillSettingsService {

    override fun getDatesToFill(stock: Stock, filler: FillerType, interval: Period): List<LocalDate> {
        return if (stock.startFill?.isBefore(LocalDate.now()) == true) {
            stock.startFill.datesUntil(LocalDate.now(), interval).toList()
                .minus(dao.getFilledDates(stock.code, filler.name).toSet())
                .toList()
        } else {
            emptyList()
        }
    }

    override fun saveResultToLog(stock: Stock, filler: FillerType, fillDate: LocalDate, result: FillResult) {
        dao.saveFillResult(stock.code, filler.name, fillDate, result.name)
    }
}