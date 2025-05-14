package com.example.tapplication.domain.repositories

import com.example.tapplication.common.utils.SortOrder
import com.example.tapplication.domain.entities.LibraryItem

interface LibraryRepository {
    suspend fun getData(page: Int, sortOrder: SortOrder): List<LibraryItem>
    suspend fun addItem(item: LibraryItem)
    suspend fun removeItem(itemId: Int): Boolean
    suspend fun updateItemAvailability(item: LibraryItem): LibraryItem?
}