package com.example.tapplication.library

import com.example.tapplication.management.Digitalizable

class Book(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val pages: Int,
    val author: String
): LibraryItem(), Digitalizable {
    override fun getDetailedInfo() = "Книга: $name ($pages стр.) автора: $author с id: $id доступна: ${if (isAvailable) "Да" else "Нет"}"
}