package com.aoverin.invest.models

import java.time.LocalDateTime

data class StockInfo(
    val code: String,
    val isActive: Boolean,
    val companyName: String,
    val assetType: String,
    val assetLocale: String,
    val stockType: String,
    val currencyName: String,
    val primaryExchange: String,
    val marketCap: Long,
    val lastUpdatedUtc: LocalDateTime
)
