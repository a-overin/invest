package com.aoverin.invest.schedulers

import com.aoverin.invest.configurations.InfoFillConfiguration
import com.aoverin.invest.dao.StockDao
import com.aoverin.invest.exceptions.RequestApiBlockingException
import com.aoverin.invest.models.FillResult
import com.aoverin.invest.models.FillerType
import com.aoverin.invest.models.Stock
import com.aoverin.invest.services.FillSettingsService
import com.aoverin.invest.services.StockMarketApi
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
@ConditionalOnProperty("com.aoverin.invest.stock.fill.info.scheduler.enabled")
class FillStockInfoScheduler(
    private val settingsService: FillSettingsService,
    private val stockMarketApi: StockMarketApi,
    private val stockDao: StockDao,
    private val fillConfiguration: InfoFillConfiguration
) {

    @Scheduled(fixedDelayString = "\${com.aoverin.invest.stock.fill.info.scheduler.fixed-rate}")
    fun fillInfo() {
        stockDao.getAllStocks()
            .also { logger.info("need fill for stocks: ${it.map(Stock::code)}") }
            .forEach { fillInfoByDates(it) }
    }

    private fun fillInfoByDates(stock: Stock) {
        logger.info("start fill info for ${stock.code}")
        runCatching {
            settingsService.getDatesToFill(stock, fillType, fillConfiguration.period)
                .also { logger.info("need fill for dates: $it") }
                .forEach { date -> fillInfo(stock, date) }
        }.onFailure { logger.error("error while fill info for ${stock.code} with message: ${it.message}") }
        logger.info("finished fill info for ${stock.code}")
    }

    private fun fillInfo(stock: Stock, date: LocalDate) {
        logger.info("start fill info for ${stock.code} and date: $date")
        runCatching {
            stockMarketApi.getStockInfoByDateAndCode(stock.code, date)
                ?.also { stockDao.saveStockInfoByDate(it, date) }
        }
            .onSuccess { settingsService.saveResultToLog(stock, fillType, date, FillResult.SUCCESS) }
            .onFailure {
                if (it is RequestApiBlockingException) {
                    logger.info("maximum api request error on ${stock.code} and $date")
                    throw it
                }
                logger.error("error while fill info for ${stock.code} and $date with message: ${it.message}")
                settingsService.saveResultToLog(stock, fillType, date, FillResult.FAILED)
            }
        logger.info("finished fill info for ${stock.code} and date: $date")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FillStockInfoScheduler::class.java)

        private val fillType = FillerType.STOCK_INFO
    }
}