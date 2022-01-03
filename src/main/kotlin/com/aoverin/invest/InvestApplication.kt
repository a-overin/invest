package com.aoverin.invest

import com.aoverin.invest.configurations.CostFillConfiguration
import com.aoverin.invest.configurations.InfoFillConfiguration
import com.aoverin.invest.configurations.PolygonApiConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableConfigurationProperties(
	PolygonApiConfiguration::class,
	InfoFillConfiguration::class,
	CostFillConfiguration::class
)
@SpringBootApplication
class InvestApplication

fun main(args: Array<String>) {
	runApplication<InvestApplication>(*args)
}
