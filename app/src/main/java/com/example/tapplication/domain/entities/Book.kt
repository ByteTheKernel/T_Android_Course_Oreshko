package com.example.tapplication.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val pages: Int,
    val author: String,
    val iconUrl: String? = null
): LibraryItem(), Parcelable {
    override fun getDetailedInfo() = "Книга: $name ($pages стр.) автора: $author с id: $id доступна: ${if (isAvailable) "Да" else "Нет"}"
}