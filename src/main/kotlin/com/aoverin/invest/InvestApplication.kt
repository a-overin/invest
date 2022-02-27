package com.aoverin.invest

import com.aoverin.invest.configurations.CostFillConfiguration
import com.aoverin.invest.configurations.InfoFillConfiguration
import com.aoverin.invest.configurations.PolygonApiProperties
import com.aoverin.invest.configurations.TelegramBotProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableConfigurationProperties(
	PolygonApiProperties::class,
	InfoFillConfiguration::class,
	CostFillConfiguration::class,
	TelegramBotProperties::class,
)
@SpringBootApplication
class InvestApplication

fun main(args: Array<String>) {
	runApplication<InvestApplication>(*args)
}
