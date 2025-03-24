package com.example.tapplication.shop

import com.example.tapplication.library.Newspaper
import java.time.Month

class NewspaperKiosk: Shop<Newspaper> {
    override fun sell(): Newspaper {
        return Newspaper(600, true, "Спорт-Экспресс", 1001, Month.JANUARY)
    }
}