package com.example.tapplication.data

import android.util.Log
import com.example.tapplication.library.Book
import com.example.tapplication.library.Disk
import com.example.tapplication.library.*
import com.example.tapplication.library.Newspaper
import com.example.tapplication.utils.SortOrder

class LibraryRepository(private val dao: LibraryDao) {
    private val pageSize = 30

    suspend fun getData(page: Int, sortOrder: SortOrder): List<LibraryItem> {
        val offset = page * pageSize
        Log.d("LibraryRepository", "Loading page $page with order by $sortOrder")
        val entities = try {
            when (sortOrder) {
                SortOrder.NAME -> dao.getItemsByName(pageSize, offset)
                SortOrder.CREATED_AT -> dao.getItemsByCreatedAt(pageSize, offset)
            }
        } catch (e: Exception) {
            Log.e("LibraryRepository", "Error during data loading", e)
            emptyList()
        }

        return entities.map { LibraryItemMapper.fromEntity(it) }
    }

    suspend fun addItem(item: LibraryItem) {
        val entity = LibraryItemMapper.toEntity(item)
        dao.insert(entity)
    }

    suspend fun removeItem(itemId: Int): Boolean {
        dao.deleteById(itemId)
        return true
    }

    suspend fun updateItemAvailability(item: LibraryItem): LibraryItem? {
        val updatedItem = when (item) {
            is Book -> item.copy(isAvailable = !item.isAvailable)
            is Disk -> item.copy(isAvailable = !item.isAvailable)
            is Newspaper -> item.copy(isAvailable = !item.isAvailable)
            else -> item
        }
        val entity = LibraryItemMapper.toEntity(updatedItem)
        dao.insert(entity)
        return updatedItem
    }
}