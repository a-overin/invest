package com.aoverin.invest.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Period

@ConstructorBinding
@ConfigurationProperties(prefix = "com.aoverin.invest.stock.fill.cost")
data class CostFillConfiguration(
    val period: Period
) : FillConfiguration {

    override fun getPeriodForFill(): Period {
        return period
    }
}