package com.example.tapplication.manager

import com.example.tapplication.library.Disk
import com.example.tapplication.library.LibraryItem
import com.example.tapplication.library.Newspaper

class LibraryManager {
    private val items = mutableListOf<LibraryItem>()

    fun addItem(item: LibraryItem) {
        items.add(item)
    }

    fun getItemsByType(type: Class<out LibraryItem>): List<LibraryItem> {
        return items.filter { it::class.java == type }
    }

    fun getItemByIndex(type: Class<out LibraryItem>, index: Int): LibraryItem? {
        val filteredItems = getItemsByType(type)
        return if (index in 1..filteredItems.size) filteredItems[index - 1] else null
    }

    fun borrowItem(item: LibraryItem, home: Boolean): String {
        return if (!item.isAvailable) {
            "Ошибка: ${item.name} уже занята!"
        } else if (item is Newspaper && home) {
            "Ошибка: Газеты нельзя брать домой!"
        } else if (item is Disk && !home) {
            "Ошибка: Диски нельзя читать в зале!"
        } else {
            item.isAvailable = false
            val action = if (home) "взяли домой" else "взяли в читальный зал"
            "${item::class.simpleName} ${item.id} $action."
        }
    }

    fun returnItem(item: LibraryItem): String {
        return if (item.isAvailable) {
            "Ошибка: ${item.name} уже доступен!"
        } else {
            item.isAvailable = true
            "${item::class.simpleName} ${item.id} возвращён"
        }
    }
}