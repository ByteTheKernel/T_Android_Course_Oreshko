package com.example.tapplication.data.network

import com.example.tapplication.library.Book

object GoogleBooksItemMapper {
    fun toLibraryItem(item: GoogleBookItem): Book? {
        val volumeInfo = item.volumeInfo ?: return null
        val title = volumeInfo.title ?: return null
        val author = volumeInfo.authors?.joinToString(", ") ?: "Unknown Author"
        val pages = volumeInfo.pageCount ?: 0
        val iconUrl = volumeInfo.imageLinks?.smallThumbnail

        val isbn = volumeInfo.industryIdentifiers
            ?.firstOrNull { it.type == "ISBN_13" || it.type == "ISBN_10" }
            ?.identifier ?: "ISBN_${title.hashCode()}"

        return Book(
            id = isbn.hashCode(),
            name = title,
            author = author,
            pages = pages,
            isAvailable = true,
            iconUrl = iconUrl
        )
    }
}