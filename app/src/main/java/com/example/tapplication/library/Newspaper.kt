package com.example.tapplication.library

import android.os.Parcelable
import com.example.tapplication.management.Digitalizable
import com.example.tapplication.utils.Month
import kotlinx.parcelize.Parcelize

@Parcelize
data class Newspaper(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val issueNumber: Int,
    val month: Month
): LibraryItem(), Digitalizable<Newspaper>, Parcelable {
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