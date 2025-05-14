package com.example.tapplication.domain.repositories

import com.example.tapplication.domain.entities.LibraryItem

interface RemoteBooksRepository {
    suspend fun searchBooksOnline(title: String?, author: String?): List<LibraryItem>
}