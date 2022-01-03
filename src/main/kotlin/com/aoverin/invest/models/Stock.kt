package com.aoverin.invest.models

import java.time.LocalDate

data class Stock(
    val code: String,
    val country: String? = null,
    val startFill: LocalDate? = null,
    val info: StockInfo?,
    val costs: List<StockCost> = emptyList(),
)
