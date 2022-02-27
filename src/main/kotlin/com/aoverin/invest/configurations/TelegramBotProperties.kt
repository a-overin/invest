package com.aoverin.invest.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "com.aoverin.invest.telegram.bot")
data class TelegramBotProperties(
    val token: String,
    val username: String,
    val chatId: String,
    val enabled: Boolean,
)
