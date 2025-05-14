package com.example.tapplication.data.repositories

import android.util.Log
import com.example.tapplication.data.datasources.local.LibraryDao
import com.example.tapplication.data.datasources.local.LibraryItemMapper
import com.example.tapplication.domain.entities.Book
import com.example.tapplication.domain.entities.Disk
import com.example.tapplication.domain.entities.LibraryItem
import com.example.tapplication.domain.entities.Newspaper
import com.example.tapplication.common.utils.SortOrder
import com.example.tapplication.domain.repositories.LibraryRepository

class LibraryRepositoryImpl(private val dao: LibraryDao): LibraryRepository {
    private val pageSize = 30

    override suspend fun getData(page: Int, sortOrder: SortOrder): List<LibraryItem> {
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

    override suspend fun addItem(item: LibraryItem) {
        val entity = LibraryItemMapper.toEntity(item)
        dao.insert(entity)
    }

    override suspend fun removeItem(itemId: Int): Boolean {
        dao.deleteById(itemId)
        return true
    }

    override suspend fun updateItemAvailability(item: LibraryItem): LibraryItem? {
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