package com.example.tapplication.data

import android.util.Log
import com.example.tapplication.library.Book
import com.example.tapplication.library.Disk
import com.example.tapplication.library.*
import com.example.tapplication.library.Newspaper
import com.example.tapplication.utils.ItemType
import com.example.tapplication.utils.Month
import com.example.tapplication.utils.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take

class LibraryRepository(private val dao: LibraryDao) {
    private val pageSize = 30
    private var currentPage = 0
    private var currentSortOrder = SortOrder.CREATED_AT


    suspend fun loadInitialData(sortOrder: SortOrder = SortOrder.CREATED_AT): List<LibraryItem> {
        currentSortOrder = sortOrder
        currentPage = 0
        return loadPage(currentPage, currentSortOrder)
    }

    suspend fun loadNextPage(): List<LibraryItem> {
        currentPage++
        return loadPage(currentPage, currentSortOrder)
    }

    suspend fun loadPreviousPage(): List<LibraryItem> {
        if (currentPage > 0) {
            currentPage--
            return loadPage(currentPage, currentSortOrder)
        }
        return emptyList()
    }

    private suspend fun loadPage(page: Int, sortOrder: SortOrder): List<LibraryItem> {
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

        return entities.map { it.toLibraryItem() }
    }

    suspend fun addItem(item: LibraryItem) {
        val entity = item.toLibraryItemEntity()
        dao.insert(entity)
    }

    suspend fun removeItem(itemId: Int): Boolean {
        dao.deleteById(itemId)
        return true
    }

    suspend fun updateItemAvailability(item: LibraryItem): LibraryItem? {
        val updateItem = when (item) {
            is Book -> item.copy(isAvailable = !item.isAvailable)
            is Disk -> item.copy(isAvailable = !item.isAvailable)
            is Newspaper -> item.copy(isAvailable = !item.isAvailable)
            else -> item
        }
        val entity = updateItem.toLibraryItemEntity()
        dao.insert(entity)
        return updateItem
    }

    private fun LibraryItemEntity.toLibraryItem(): LibraryItem {
        return when(type) {
            ItemType.BOOK.name -> {
                val parts = details.split("|")
                Book(
                    id = id,
                    isAvailable = isAvailable,
                    name = name,
                    pages = parts[0].toInt(),
                    author = parts[1]
                )
            }
            ItemType.NEWSPAPER.name -> {
                val parts = details.split("|")
                Newspaper(
                    id = id,
                    isAvailable = isAvailable,
                    name = name,
                    issueNumber = parts[0].toInt(),
                    month = Month.valueOf(parts[1])
                )
            }
            ItemType.DISK.name -> Disk(
                id = id,
                isAvailable = isAvailable,
                name = name,
                type = details
            )
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    private fun LibraryItem.toLibraryItemEntity(): LibraryItemEntity {
        return LibraryItemEntity(
            id = id,
            type = when (this) {
                is Book -> ItemType.BOOK.name
                is Newspaper -> ItemType.NEWSPAPER.name
                is Disk -> ItemType.DISK.name
                else -> throw IllegalArgumentException("Unknown item type")
            },
            name = name,
            isAvailable = isAvailable,
            details = when (this) {
                is Book -> "$pages|$author"
                is Newspaper -> "$issueNumber|${month.name}"
                is Disk -> type
                else -> ""
            },
            createdAt = System.currentTimeMillis()
        )
    }
}