package com.aoverin.invest.schedulers

import com.aoverin.invest.configurations.CostFillConfiguration
import com.aoverin.invest.dao.StockDao
import com.aoverin.invest.models.FillerType
import com.aoverin.invest.models.Stock
import com.aoverin.invest.services.FillSettingsService
import com.aoverin.invest.services.StockMarketApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
@ConditionalOnProperty("com.aoverin.invest.stock.fill.cost.scheduler.enabled")
class FillStockCostScheduler(
    private val stockMarketApi: StockMarketApi,
    private val stockDao: StockDao,
    settingsService: FillSettingsService,
    fillConfiguration: CostFillConfiguration
) : AbstractFillScheduler(fillConfiguration, settingsService) {

    override val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelayString = "\${com.aoverin.invest.stock.fill.info.scheduler.fixed-rate}")
    fun fillCosts() {
        runCatching {
            stockDao.getAllStocks()
                .also { logger.info("need fill for stocks cost: ${it.map(Stock::code)}") }
                .forEach { fillInfoByDates(it) }
        }.onFailure { logger.error("error while fill cost with message: ${it.message}") }
    }

    override fun makeWorkForStockByDate(stock: Stock, date: LocalDate) {
        stockMarketApi.getStockCostByDateAndCode(stock.code, date)
            ?.also { stockDao.saveStockCost(it) }
    }

    override fun getFillType() = FillerType.STOCK_COSTS
}