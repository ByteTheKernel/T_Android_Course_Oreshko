package com.example.tapplication.library

class Newspaper(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val issueNumber: Int
): LibraryItem {
    override fun getShortInfo() = "$name доступна: ${if (isAvailable) "Да" else "Нет"}"
    override fun getDetailedInfo() = "Выпуск: $issueNumber газеты $name с id: $id доступен: ${if (isAvailable) "Да" else "Нет"}"
}