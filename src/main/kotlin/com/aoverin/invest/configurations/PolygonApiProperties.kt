package com.aoverin.invest.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "com.aoverin.invest.market.polygon")
data class PolygonApiProperties(
    val apiKey: String,
    val url: String
)