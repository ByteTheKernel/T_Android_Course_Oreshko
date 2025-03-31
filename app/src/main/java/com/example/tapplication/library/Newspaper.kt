package com.example.tapplication.library

import com.example.tapplication.management.Digitalizable
import com.example.tapplication.utils.Month

class Newspaper(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val issueNumber: Int,
    val month: Month
): LibraryItem(), Digitalizable<Newspaper> {
    override fun getDetailedInfo(): String{
        return "Выпуск: $issueNumber (${month.russianMonth}) газеты $name с id: $id доступен: ${if (isAvailable) "Да" else "Нет"}"
    }

    override fun takeHome(): String {
        return "Ошибка: Газеты нельзя брать домой!"
    }

    override fun toDisk(newId: Int): Disk {
        return Disk(
            id = newId,
            isAvailable = true,
            name = "Цифровая копия газеты: $name (${month.russianMonth})",
            type = "CD"
        )
    }
}