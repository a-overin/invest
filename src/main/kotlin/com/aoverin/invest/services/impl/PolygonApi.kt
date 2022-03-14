package com.aoverin.invest.services.impl

import com.aoverin.invest.configurations.PolygonApiProperties
import com.aoverin.invest.exceptions.RequestApiBlockingException
import com.aoverin.invest.models.StockCost
import com.aoverin.invest.models.StockInfo
import com.aoverin.invest.services.StockMarketApi
import com.aoverin.invest.services.impl.models.StockCostResponse
import com.aoverin.invest.services.impl.models.StockInfoData
import com.aoverin.invest.services.impl.models.StockInfoResponse
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class PolygonApi(
    restTemplateBuilder: RestTemplateBuilder,
    private val configuration: PolygonApiProperties
) : StockMarketApi {

    private val restTemplate = restTemplateBuilder.build()

    override fun getStockCostByDateAndCode(code: String, date: LocalDate): StockCost? {
        val responseEntity = executeRequest(
            UriComponentsBuilder.fromUriString(configuration.url + DAILY_OPEN_CLOSE)
                .queryParam("apiKey", configuration.apiKey)
                .buildAndExpand(
                    mapOf(
                        "stocksTicker" to code,
                        "date" to date.format(DateTimeFormatter.ISO_DATE)
                    )
                )
                .toUri(),
            StockCostResponse::class.java,
        )
        return if (responseEntity.statusCode == HttpStatus.OK && responseEntity.body?.status == "OK") {
            responseEntity.body?.toStockCost()
        } else {
            null
        }
    }

    override fun getStockInfoByDateAndCode(code: String, date: LocalDate): StockInfo? {
        val responseEntity = executeRequest(
            UriComponentsBuilder.fromUriString(configuration.url + TICKER_DETAIL)
                .queryParam("apiKey", configuration.apiKey)
                .queryParam("date", date.format(DateTimeFormatter.ISO_DATE))
                .buildAndExpand(mapOf("ticker" to code)).toUri(),
            StockInfoResponse::class.java
        )
        return if (responseEntity.statusCode == HttpStatus.OK && responseEntity.body?.status == "OK") {
            responseEntity.body?.results?.toStockInfo()
        } else {
            return null
        }
    }

    private fun <T> executeRequest(uri: URI, clazz: Class<T>): ResponseEntity<T> {
        try {
            return restTemplate.getForEntity(uri, clazz)
        } catch (e: HttpClientErrorException) {
            if (e.statusCode in ignoredErrorCodesList) {
                throw RequestApiBlockingException(e.message, e)
            } else {
                throw e
            }
        }
    }

    private fun StockInfoData.toStockInfo(): StockInfo {
        return StockInfo(
            code = this.ticker!!,
            isActive = this.active!!,
            companyName = this.name!!,
            assetType = this.market!!,
            assetLocale = this.locale!!,
            stockType = this.type!!,
            currencyName = this.currencyName!!,
            primaryExchange = this.primaryExchange!!,
            marketCap = this.marketCap!!,
            lastUpdatedUtc = LocalDateTime.now()
        )
    }

    private fun StockCostResponse.toStockCost(): StockCost {
        return StockCost(
            code = this.symbol!!,
            date = this.from!!,
            open = this.open!!,
            close = this.close!!,
            max = this.high!!,
            min = this.low!!,
            preMarket = this.preMarket!!,
            afterHours = this.afterHours!!,
        )
    }

    companion object {

        private val ignoredErrorCodesList = listOf(
            HttpStatus.TOO_MANY_REQUESTS,
            HttpStatus.BAD_REQUEST
        )

        private const val DAILY_OPEN_CLOSE = "/v1/open-close/{stocksTicker}/{date}"

        private const val TICKER_DETAIL = "/v3/reference/tickers/{ticker}"
    }
}