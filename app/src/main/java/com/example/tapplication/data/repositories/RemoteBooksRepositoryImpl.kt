package com.example.tapplication.data.repositories

import android.util.Log
import com.example.tapplication.data.datasources.network.GoogleBooksApi
import com.example.tapplication.data.datasources.network.GoogleBooksItemMapper
import com.example.tapplication.domain.entities.LibraryItem
import com.example.tapplication.domain.repositories.RemoteBooksRepository

class RemoteBooksRepositoryImpl(
    private val api: GoogleBooksApi
): RemoteBooksRepository {
    override suspend fun searchBooksOnline(title: String?, author: String?): List<LibraryItem> {
        if (title.isNullOrBlank() && author.isNullOrBlank()) {
            Log.w("RemoteBooksRepository", "Empty title and author — skipping request")
            return emptyList()
        }

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
            throw e
        }
    }
}