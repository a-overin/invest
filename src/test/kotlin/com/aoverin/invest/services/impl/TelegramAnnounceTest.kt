package com.aoverin.invest.services.impl

import com.aoverin.invest.services.AnnounceService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class TelegramAnnounceTest {

    @Autowired
    private lateinit var announceService: AnnounceService

    @Test
    fun test() {
//        announceService.sendAnnounce("test")
    }
}