package com.example.tapplication.data.repository

import android.util.Log
import com.example.tapplication.data.network.GoogleBooksApi
import com.example.tapplication.data.network.GoogleBooksItemMapper
import com.example.tapplication.library.Book
import com.example.tapplication.library.LibraryItem

class RemoteBooksRepository(
    private val api: GoogleBooksApi
) {
    suspend fun searchBooksOnline(title: String?, author: String?): List<LibraryItem> {
        val query = buildString {
            if (!title.isNullOrBlank()) append("intitle:$title ")
            if (!author.isNullOrBlank()) append("inauthor:$author")
        }.trim()

        return try {
            val response = api.searchBooks(query)
            Log.d("RemoteBooksRepository", "Items count: ${response.items?.size ?: 0}")
            response.items.orEmpty().mapNotNull {
                val mapped = GoogleBooksItemMapper.toLibraryItem(it)
                Log.d("RemoteBooksRepository", "Mapped item: $mapped")
                mapped
            }

        } catch (e: Exception) {
            Log.e("RemoteBooksRepository", "Error loading books", e)
            emptyList()
        }
    }

}