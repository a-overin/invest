package com.aoverin.invest.models

import java.time.LocalDate

data class StockCost(
    val code: String,
    val date: LocalDate,
    val open: Double,
    val close: Double,
    val max: Double,
    val min: Double,
    val preMarket: Double,
    val afterHours: Double
)
