package com.example.tapplication.manager

import com.example.tapplication.library.LibraryItem

class LibraryManager {
    private val items = mutableListOf<LibraryItem>()

    fun addItems(items: List<LibraryItem>) {
        this.items.addAll(items)
    }

    fun getItemsByType(type: Class<out LibraryItem>): List<LibraryItem> {
        return items.filterIsInstance(type)
    }

    fun getItemById(id: Int): LibraryItem? {
        return items.firstOrNull { it.id == id }
    }
}