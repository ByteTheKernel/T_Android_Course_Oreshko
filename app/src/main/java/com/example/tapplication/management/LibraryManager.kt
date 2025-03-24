package com.example.tapplication.management

import com.example.tapplication.library.LibraryItem

class LibraryManager {
    private val items = mutableListOf<LibraryItem>()
    val allItems: List<LibraryItem>
        get() = items.toList()

    fun addListItems(items: List<LibraryItem>) {
        this.items.addAll(items)
    }

    fun addItems(vararg items: LibraryItem) {
        this.items.addAll(items)
    }

    inline fun<reified T: LibraryItem> getItemsByType(): List<T> {
        return allItems.filterIsInstance<T>()
    }

    fun getItemById(id: Int): LibraryItem? {
        return items.firstOrNull { it.id == id }
    }
}