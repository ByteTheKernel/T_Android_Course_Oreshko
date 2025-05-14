package com.example.tapplication.domain.entities

import android.os.Parcelable
import com.example.tapplication.common.utils.Month
import kotlinx.parcelize.Parcelize

@Parcelize
data class Newspaper(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val issueNumber: Int,
    val month: Month
): LibraryItem(), Parcelable {
    override fun getDetailedInfo(): String{
        return "Выпуск: $issueNumber (${month.russianMonth}) газеты $name с id: $id доступен: ${if (isAvailable) "Да" else "Нет"}"
    }

    override fun takeHome(): String {
        return "Ошибка: Газеты нельзя брать домой!"
    }
}