package com.example.tapplication.library

import com.example.tapplication.management.Digitalizable
import java.time.Month

class Newspaper(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val issueNumber: Int,
    val month: Month
): LibraryItem(), Digitalizable {
    override fun getDetailedInfo(): String{
        val monthInRussian = getRussianMonth(month)
        return "Выпуск: $issueNumber ($monthInRussian) газеты $name с id: $id доступен: ${if (isAvailable) "Да" else "Нет"}"
    }

    override fun takeHome(): String {
        return "Ошибка: Газеты нельзя брать домой!"
    }

    private fun getRussianMonth(month: Month): String {
        val months = listOf(
            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
        )
        return months[month.value-1]
    }
}