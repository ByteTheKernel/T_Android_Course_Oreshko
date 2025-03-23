package com.example.tapplication.library

class Newspaper(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val issueNumber: Int
): LibraryItem() {
    override fun getDetailedInfo() = "Выпуск: $issueNumber газеты $name с id: $id доступен: ${if (isAvailable) "Да" else "Нет"}"

    override fun takeHome(): String {
        return "Ошибка: Газеты нельзя брать домой!"
    }
}