package com.example.tapplication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tapplication.library.Book
import com.example.tapplication.library.Disk
import com.example.tapplication.library.LibraryItem
import com.example.tapplication.library.Newspaper
import com.example.tapplication.utils.Month
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.*

class MainViewModel: ViewModel() {
    private val _items = MutableLiveData<List<LibraryItem>>()
    val items: LiveData<List<LibraryItem>> = _items

    private val _selectedItem = MutableLiveData<LibraryItem?>()
    val selectedItem: LiveData<LibraryItem?> = _selectedItem

    private val _selectedItemId = MutableLiveData<Int?>()
    val selectedItemId: LiveData<Int?> = _selectedItemId

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _scrollPosition = MutableLiveData<Int>()
    val scrollPosition: LiveData<Int> = _scrollPosition

    init {
        loadItems()
    }

    fun addItem(newItem: LibraryItem) {
        val currentList = _items.value?.toMutableList() ?: mutableListOf()
        currentList.add(newItem)
        _items.value = currentList
    }

    fun updateItemAvailability(item: LibraryItem) {
        val currentList = _items.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            val updatedItem = when (item) {
                is Book -> item.copy(isAvailable = !item.isAvailable)
                is Disk -> item.copy(isAvailable = !item.isAvailable)
                is Newspaper -> item.copy(isAvailable = !item.isAvailable)
                else -> item
            }
            currentList[index] = updatedItem
            _items.value = currentList
            _toastMessage.value = "Элемент с id #${item.id}"
        }
    }

    fun onToastShown() {
        _toastMessage.value = null
    }

    fun removeItem(position: Int) {
        val currentList = _items.value?.toMutableList() ?: return
        currentList.removeAt(position)
        _items.value = currentList
    }

    fun getItemById(itemId: Int): LibraryItem? {
        return _items.value?.find { it.id == itemId }
    }

    fun getSelectedItem(): LibraryItem? {
        val itemId = _selectedItemId.value ?: return null
        return _items.value?.find { it.id == itemId }
    }

    fun selectItem(item: LibraryItem) {
        _selectedItemId.value = item.id
    }

    fun clearSelectedItem() {
        _selectedItemId.value = null
    }

    fun saveScrollPosition(position: Int) {
        _scrollPosition.value = position
    }

    fun restoreScrollPosition(): Int {
        return _scrollPosition.value ?: 0
    }

    private fun loadItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val loadedItems = withContext(Dispatchers.IO) {
                    simulateNetworkOperation {
                        generateDummyData()
                    }
                }
                _items.value = loadedItems
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun <T> simulateNetworkOperation(block: () -> T): T {
        delay((1000..2000).random().toLong())

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
}