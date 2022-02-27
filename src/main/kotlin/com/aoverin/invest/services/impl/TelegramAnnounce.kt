package com.aoverin.invest.services.impl

import com.aoverin.invest.configurations.TelegramBotProperties
import com.aoverin.invest.services.AnnounceService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class TelegramAnnounce(
    val botProperties: TelegramBotProperties
) : AnnounceService, TelegramLongPollingBot() {

    override fun sendAnnounce(announce: String) {
        sendMessage(announce)
    }

    override fun getBotToken() = botProperties.token

    override fun getBotUsername() = botProperties.username

    override fun onUpdateReceived(update: Update?) {
        println(update)
    }

    private fun sendMessage(message: String): Message? {
        return if (botProperties.enabled) {
            println("send message: $message")
            execute(
                SendMessage().apply {
                    chatId = botProperties.chatId
                    text = message
                }
            )
        } else
            null
    }
}