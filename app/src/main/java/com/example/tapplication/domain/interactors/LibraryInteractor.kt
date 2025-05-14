package com.example.tapplication.domain.interactors

import com.example.tapplication.common.utils.SortOrder
import com.example.tapplication.domain.entities.LibraryItem
import com.example.tapplication.domain.usecases.*

class LibraryInteractor(
    private val loadItemsUseCase: LoadItemsUseCase,
    private val addItemUseCase: AddItemUseCase,
    private val updateItemStatusUseCase: UpdateItemStatusUseCase,
    private val removeItemUseCase: RemoveItemUseCase,
    private val searchOnlineUseCase: SearchOnlineUseCase,
    private val saveSortOrderUseCase: SaveSortOrderUseCase,
    private val getSavedSortOrderUseCase: GetSavedSortOrderUseCase
) {
    suspend fun loadItems(page: Int = 0, sortOrder: SortOrder = SortOrder.CREATED_AT): List<LibraryItem> {
        return loadItemsUseCase(page, sortOrder)
    }

    suspend fun addItem(item: LibraryItem) {
        addItemUseCase(item)
    }

    suspend fun updateItemStatus(item: LibraryItem): LibraryItem? {
        return updateItemStatusUseCase(item)
    }

    suspend fun removeItem(itemId: Int): Boolean {
        return removeItemUseCase(itemId)
    }

    suspend fun searchOnline(title: String?, author: String?): List<LibraryItem> {
        return searchOnlineUseCase(title, author)
    }

    fun saveSortOrder(sortOrder: SortOrder) {
        saveSortOrderUseCase(sortOrder)
    }

    fun getSavedSortOrder(): SortOrder {
        return getSavedSortOrderUseCase()
    }
}