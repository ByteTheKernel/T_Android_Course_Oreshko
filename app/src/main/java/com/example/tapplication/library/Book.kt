package com.example.tapplication.library

class Book(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val pages: Int,
    val author: String
): LibraryItem {
    override fun getShortInfo() = "$name доступна: ${if (isAvailable) "Да" else "Нет"}"
    override fun getDetailedInfo() = "Книга: $name ($pages стр.) автора: $author с id: $id доступна: ${if (isAvailable) "Да" else "Нет"}"
}