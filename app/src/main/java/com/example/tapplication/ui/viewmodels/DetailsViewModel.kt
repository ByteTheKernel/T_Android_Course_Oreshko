package com.example.tapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.tapplication.library.*
import com.example.tapplication.library.LibraryItem
import com.example.tapplication.utils.ItemType
import com.example.tapplication.utils.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailsViewModel: ViewModel() {

    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> = _formState.asStateFlow()

    private val _createdItem = MutableStateFlow<LibraryItem?>(null)
    val createdItem: StateFlow<LibraryItem?> = _createdItem.asStateFlow()

    fun updateType(type: ItemType) {
        _formState.value = _formState.value.copy(type = type)
    }

    fun updateName(name: String) {
        _formState.value = _formState.value.copy(name = name)
    }

    fun updateId(id: Int) {
        _formState.value = _formState.value.copy(id = id)
    }

    fun updateField1(field1: String) {
        _formState.value = _formState.value.copy(field1 = field1)
    }

    fun updateField2(field2: String) {
        _formState.value = _formState.value.copy(field2 = field2)
    }

    fun createItemFromInput() {
        val currentState = _formState.value
        val newItem = when (currentState.type) {
            ItemType.BOOK -> Book(
                id = currentState.id,
                isAvailable = true,
                name = currentState.name,
                pages = currentState.field2.toIntOrNull() ?: 0,
                author = currentState.field1
            )

            ItemType.DISK -> Disk(
                id = currentState.id,
                isAvailable = true,
                name = currentState.name,
                type = currentState.field1
            )

            ItemType.NEWSPAPER -> {
                val month = Month.entries.firstOrNull {
                    it.russianMonth.equals(currentState.field2, ignoreCase = true)
                } ?: return
                Newspaper(
                    id = currentState.id,
                    isAvailable = true,
                    name = currentState.name,
                    issueNumber = currentState.field1.toIntOrNull() ?: 0,
                    month = month
                )
            }
        }
        _createdItem.value = newItem
    }

    fun resetForm() {
        _formState.value = FormState()
        _createdItem.value = null
    }
}

data class FormState(
    val type: ItemType = ItemType.BOOK,
    val name: String = "",
    val id: Int = 0,
    val field1: String = "",
    val field2: String = ""
)