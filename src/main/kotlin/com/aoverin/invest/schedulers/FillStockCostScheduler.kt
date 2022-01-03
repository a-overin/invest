package com.aoverin.invest.schedulers

import com.aoverin.invest.configurations.CostFillConfiguration
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
@ConditionalOnProperty("com.aoverin.invest.stock.fill.cost.scheduler.enabled")
class FillStockCostScheduler(
    private val settingsService: FillSettingsService,
    private val stockMarketApi: StockMarketApi,
    private val stockDao: StockDao,
    private val fillConfiguration: CostFillConfiguration
) {

    @Scheduled(fixedDelayString = "\${com.aoverin.invest.stock.fill.info.scheduler.fixed-rate}")
    fun fillCosts() {
        stockDao.getAllStocks()
            .also { logger.info("need fill for stocks: ${it.map(Stock::code)}") }
            .forEach { fillInfoByDates(it) }
    }

    private fun fillInfoByDates(stock: Stock) {
        logger.info("start fill cost for ${stock.code}")
        runCatching {
            settingsService.getDatesToFill(stock, fillType, fillConfiguration.period)
                .also { logger.info("need fill for dates: $it") }
                .forEach { date -> fillCosts(stock, date) }
        }.onFailure { logger.error("error while fill cost for ${stock.code} with message: ${it.message}") }
        logger.info("finished fill info for ${stock.code}")
    }

    private fun fillCosts(stock: Stock, date: LocalDate) {
        logger.info("start fill cost for ${stock.code} and date: $date")
        runCatching {
            stockMarketApi.getStockCostByDateAndCode(stock.code, date)
                ?.also { stockDao.saveStockCost(it) }
        }
            .onSuccess { settingsService.saveResultToLog(stock, fillType, date, FillResult.SUCCESS) }
            .onFailure {
                if (it is RequestApiBlockingException) {
                    logger.info("maximum api request error on ${stock.code} and $date")
                    throw it
                }
                logger.error("error while fill cost for ${stock.code} and $date with message: ${it.message}")
                settingsService.saveResultToLog(stock, fillType, date, FillResult.FAILED)
            }
        logger.info("finished fill cost for ${stock.code} and date: $date")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FillStockCostScheduler::class.java)

        private val fillType = FillerType.STOCK_COSTS
    }
}