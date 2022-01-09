package com.aoverin.invest.configurations

import java.time.Period

interface FillConfiguration {
    fun getPeriodForFill(): Period
}