package com.aoverin.invest.schedulers

import com.aoverin.invest.configurations.InfoFillConfiguration
import com.aoverin.invest.dao.StockDao
import com.aoverin.invest.models.FillerType
import com.aoverin.invest.models.Stock
import com.aoverin.invest.services.FillSettingsService
import com.aoverin.invest.services.StockMarketApi
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
@ConditionalOnProperty("com.aoverin.invest.stock.fill.info.scheduler.enabled")
class FillStockInfoScheduler(
    private val stockMarketApi: StockMarketApi,
    private val stockDao: StockDao,
    settingsService: FillSettingsService,
    fillConfiguration: InfoFillConfiguration
) : AbstractFillScheduler(fillConfiguration, settingsService) {

    @Scheduled(fixedDelayString = "\${com.aoverin.invest.stock.fill.info.scheduler.fixed-rate}")
    fun fillInfo() {
        runCatching {
            stockDao.getAllStocks()
                .also { logger.info("need fill for stocks info: ${it.map(Stock::code)}") }
                .forEach { fillInfoByDates(it) }
        }.onFailure { logger.error("error while fill info with message: ${it.message}") }
    }

    override fun makeWorkForStockByDate(stock: Stock, date: LocalDate) {
        stockMarketApi.getStockInfoByDateAndCode(stock.code, date)
            ?.also { stockDao.saveStockInfoByDate(it, date) }
    }

    override fun getFillType() = FillerType.STOCK_INFO
}