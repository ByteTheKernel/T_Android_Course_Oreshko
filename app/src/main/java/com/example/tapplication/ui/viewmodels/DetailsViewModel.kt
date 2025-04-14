package com.example.tapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.tapplication.library.*
import com.example.tapplication.library.LibraryItem
import com.example.tapplication.utils.ItemType
import com.example.tapplication.utils.*

class DetailsViewModel: ViewModel() {

    var currentItem: LibraryItem? = null
    var isEditMode: Boolean = false
    var newItemType: ItemType? = null

    fun createItemFromInput(
        type: ItemType,
        name: String,
        id: Int,
        field1: String,
        field2: String
    ): LibraryItem? {
        return when (type) {
            ItemType.BOOK -> Book(id, true, name, field2.toIntOrNull() ?: 0, field1)
            ItemType.DISK -> Disk(id, true, name, field1)
            ItemType.NEWSPAPER -> {
                val month = Month.entries.firstOrNull {
                    it.russianMonth.equals(field2, ignoreCase = true)
                } ?: return null
                Newspaper(id, true, name, field1.toIntOrNull() ?: 0, month)
            }
        }
    }
}