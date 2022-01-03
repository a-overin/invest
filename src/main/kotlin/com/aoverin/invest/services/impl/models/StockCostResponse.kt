package com.aoverin.invest.services.impl.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class StockCostResponse(
    val status: String? = null,
    val from: LocalDate? = null,
    val symbol: String? = null,
    val open: Double? = null,
    val close: Double? = null,
    val high: Double? = null,
    val low: Double? = null,
    @JsonProperty("preMarket")
    val preMarket: Double? = null,
    @JsonProperty("afterHours")
    val afterHours: Double? = null
)
