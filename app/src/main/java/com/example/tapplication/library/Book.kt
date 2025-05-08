package com.example.tapplication.library

import android.os.Parcelable
import com.example.tapplication.management.Digitalizable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val pages: Int,
    val author: String,
    val iconUrl: String? = null
): LibraryItem(), Digitalizable<Book>, Parcelable {
    override fun getDetailedInfo() = "Книга: $name ($pages стр.) автора: $author с id: $id доступна: ${if (isAvailable) "Да" else "Нет"}"

    override fun toDisk(newId: Int): Disk {
        return Disk(
            id = newId,
            isAvailable = true,
            name = "Цифровая копия книги: $name",
            type = "CD"
        )
    }
}