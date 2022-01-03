package com.aoverin.invest.dao.impl

import com.aoverin.invest.dao.LogFillDao
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class LogFillDaoImpl(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : LogFillDao {

    override fun getFilledDates(stockCode: String, fillerTypeName: String): List<LocalDate> {
        return jdbcTemplate.queryForList(
            SQL_GET_DATES,
            mapOf("stock_code" to stockCode,
                "fill_type" to fillerTypeName),
            LocalDate::class.java
        )
    }

    override fun saveFillResult(stockCode: String, fillerTypeName: String, fillDate: LocalDate, result: String) {
        SimpleJdbcInsert(jdbcTemplate.jdbcTemplate)
            .withTableName("stock_fill_info")
            .execute(mapOf(
                "stock_code" to stockCode,
                "fill_type" to fillerTypeName,
                "fill_date" to fillDate,
                "fill_status" to result
            ))
    }

    companion object {
        private const val SQL_GET_DATES =
            """select fill_date
                 from stock_fill_info
                where stock_code = :stock_code and fill_type = :fill_type"""
    }
}