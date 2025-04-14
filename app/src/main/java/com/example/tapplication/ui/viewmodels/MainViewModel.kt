package com.example.tapplication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tapplication.library.Book
import com.example.tapplication.library.Disk
import com.example.tapplication.library.LibraryItem
import com.example.tapplication.library.Newspaper
import com.example.tapplication.utils.Month

class MainViewModel: ViewModel() {
    private val _items = MutableLiveData<List<LibraryItem>>()
    val items: LiveData<List<LibraryItem>> = _items

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    init {
        _items.value = listOf(
            Book(101, true, "Мастер и Маргарита", 500, "М. Булгаков"),
            Book(102, true, "Преступление и наказание", 672, "Ф. Достоевский"),
            Newspaper(201, true, "Коммерсант", 789, Month.MARCH),
            Newspaper(202, true, "Известия", 1023, Month.FEBRUARY),
            Disk(301, true, "Интерстеллар", "DVD"),
            Disk(302, true, "Пинк Флойд - The Wall", "CD")
        )
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
}