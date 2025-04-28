package com.example.tapplication.data

import android.util.Log
import com.example.tapplication.library.*
import com.example.tapplication.utils.Month
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LibraryRepository {

    private val _items = mutableListOf<LibraryItem>()
    private var isInitialized = false

    private val _itemsFlow = MutableStateFlow<List<LibraryItem>>(emptyList())
    val itemsFlow: StateFlow<List<LibraryItem>> = _itemsFlow.asStateFlow()

    suspend fun removeItem(position: Int) {
        simulateNetworkOperation {
            if (position in _items.indices) {
                _items.removeAt(position)
                updateItemsFlow()
            }
        }
    }

    private fun updateItemsFlow() {
        _itemsFlow.value = _items.toList()
    }

    suspend fun initializeIfNeeded() {
        simulateNetworkOperation {
            if (!isInitialized) {
                initializeItems()
                isInitialized = true
            }
        }
    }

    suspend fun addItem(newItem: LibraryItem) {
        simulateNetworkOperation {
            _items.add(newItem)
            updateItemsFlow()
            Log.d("Repository", "Added new item: $newItem")
        }
    }

    suspend fun updateItemAvailability(item: LibraryItem): LibraryItem? {
        return simulateNetworkOperation {
            val index = _items.indexOfFirst { it.id == item.id }
            if (index != -1) {
                val updatedItem = when (item) {
                    is Book -> item.copy(isAvailable = !item.isAvailable)
                    is Disk -> item.copy(isAvailable = !item.isAvailable)
                    is Newspaper -> item.copy(isAvailable = !item.isAvailable)
                    else -> item
                }
                _items[index] = updatedItem
                updateItemsFlow()
                Log.d("Repository", "Updated item: $updatedItem")
                updatedItem
            } else {
                Log.d("Repository", "Item not found for update: ${item.id}")
                null
            }
        }
    }

    private suspend fun <T> simulateNetworkOperation(block: () -> T): T {
        delay((100..2000).random().toLong())

        if ((1..5).random() == 1) {
            throw RuntimeException("Simulated error during data loading")
        }

        return block()
    }

    private fun generateDummyData(): List<LibraryItem> {
        return listOf(
            Book(101, true, "Мастер и Маргарита", 500, "М. Булгаков"),
            Book(102, true, "Преступление и наказание", 672, "Ф. Достоевский"),
            Newspaper(201, true, "Коммерсант", 789, Month.MARCH),
            Newspaper(202, true, "Известия", 1023, Month.FEBRUARY),
            Disk(301, true, "Интерстеллар", "DVD"),
            Disk(302, true, "Пинк Флойд - The Wall", "CD")
        )
    }

    private fun initializeItems() {
        _items.addAll(generateDummyData())
        updateItemsFlow()
        Log.d("Repository", "Initialized items: ${_items.size}")
    }
}