package com.aoverin.invest.services.impl.models

import com.fasterxml.jackson.annotation.JsonProperty

data class StockInfoData(
    val ticker: String? = null,
    val active: Boolean? = null,
    val name: String? = null,
    val market: String? = null,
    val locale: String? = null,
    val type: String? = null,
    @JsonProperty("currency_name")
    val currencyName: String? = null,
    @JsonProperty("primary_exchange")
    val primaryExchange: String? = null,
    @JsonProperty("market_cap")
    val marketCap: Long? = null
)