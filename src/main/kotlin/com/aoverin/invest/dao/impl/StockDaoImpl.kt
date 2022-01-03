package com.aoverin.invest.dao.impl

import com.aoverin.invest.dao.StockDao
import com.aoverin.invest.models.Stock
import com.aoverin.invest.models.StockCost
import com.aoverin.invest.models.StockInfo
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class StockDaoImpl(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : StockDao {

    override fun getAllStocks(): List<Stock> {
        return jdbcTemplate.jdbcTemplate.query(
            SQL_GET_STOCK_ALL
        ) { rs, _ -> Stock(
            code = rs.getString("code"),
            country = rs.getString("country"),
            startFill = rs.getDate("start_fill")?.toLocalDate(),
            getCurrentStockInfo(rs.getString("code")))
        }
    }

    override fun saveStockInfoByDate(stockInfo: StockInfo, date: LocalDate) {
        jdbcTemplate.update(
            SQL_ADD_STOCK_INFO,
            mapOf(
                "load_date" to date,
                "stock_code" to stockInfo.code,
                "active" to stockInfo.isActive,
                "company_name" to stockInfo.companyName,
                "asset_type" to stockInfo.assetType,
                "asset_locale" to stockInfo.assetLocale,
                "stock_type" to stockInfo.stockType,
                "currency_name" to stockInfo.currencyName,
                "primary_exchange" to stockInfo.primaryExchange,
                "market_cap" to stockInfo.marketCap,
                "last_updated_utc" to stockInfo.lastUpdatedUtc,
            )
        )
    }

    override fun saveStockCost(stockCost: StockCost) {
        jdbcTemplate.update(
            SQL_ADD_STOCK_COST,
            mapOf(
                "load_date" to stockCost.date,
                "stock_code" to stockCost.code,
                "open" to stockCost.open,
                "close" to stockCost.close,
                "max" to stockCost.max,
                "min" to stockCost.min,
                "pre_market" to stockCost.preMarket,
                "after_hours" to stockCost.afterHours
            )
        )
    }

    private fun getCurrentStockInfo(stockCode: String): StockInfo? {
        return runCatching {
            return jdbcTemplate.queryForObject(
                SQL_GET_CURRENT_STOCK_INFO,
                mapOf("stock_code" to stockCode)
            ) { rs, _ ->
                StockInfo(
                    code = stockCode,
                    isActive = rs.getBoolean("active"),
                    companyName = rs.getString("company_name"),
                    assetType = rs.getString("asset_type"),
                    assetLocale = rs.getString("asset_locale"),
                    stockType = rs.getString("stock_type"),
                    currencyName = rs.getString("currency_name"),
                    primaryExchange = rs.getString("primary_exchange"),
                    marketCap = rs.getLong("market_cap"),
                    lastUpdatedUtc = rs.getTimestamp("last_updated_utc").toLocalDateTime()
                )
            }
        }.getOrNull()
    }

    companion object {
        private const val SQL_GET_STOCK_ALL = "select code, country, start_fill from stocks"

        private const val SQL_GET_CURRENT_STOCK_INFO =
            """select stock_code, load_date, active, company_name, asset_type, asset_locale, stock_type, currency_name, primary_exchange, market_cap, last_updated_utc
                 from stocks_info_cur where stock_code = :stock_code"""

        private const val SQL_ADD_STOCK_INFO =
            """insert into stocks_info (stock_code, load_date, active, company_name, asset_type, asset_locale, stock_type, currency_name, primary_exchange, market_cap, last_updated_utc)
                values (:stock_code, :load_date, :active, :company_name, :asset_type, :asset_locale, :stock_type, :currency_name, :primary_exchange, :market_cap, :last_updated_utc)
                on conflict on constraint stocks_info_pk do update set active = excluded.active, company_name = excluded.company_name"""

        private const val SQL_ADD_STOCK_COST =
            """insert into stocks_costs (stock_code, load_date, open, close, max, min, pre_market, after_hours)
                values (:stock_code, :load_date, :open, :close, :max, :min, :pre_market, :after_hours)
                on conflict on constraint stocks_costs_pk do update set open = excluded.open, close = excluded.close, max = excluded.max, min = excluded.min, pre_market = excluded.pre_market, after_hours = excluded.after_hours"""
    }
}