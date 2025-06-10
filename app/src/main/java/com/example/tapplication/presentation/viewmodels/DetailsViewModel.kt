package com.example.tapplication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.tapplication.domain.entities.LibraryItem
import com.example.tapplication.common.utils.ItemType
import com.example.tapplication.common.utils.Month
import com.example.tapplication.domain.entities.Book
import com.example.tapplication.domain.entities.Disk
import com.example.tapplication.domain.entities.Newspaper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class DetailsViewModel @Inject constructor() : ViewModel() {

    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> = _formState.asStateFlow()

    private val _createdItem = MutableStateFlow<LibraryItem?>(null)
    val createdItem: StateFlow<LibraryItem?> = _createdItem.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun updateType(type: ItemType) {
        _formState.value = _formState.value.copy(type = type)
    }

    fun resetForm() {
        _formState.value = FormState()
        _createdItem.value = null
        _errorMessage.value = null
    }

    fun createItemFromInputDirectly(
        name: String,
        id: Int,
        field1: String,
        field2: String
    ) {
        val type = _formState.value.type
        when (type) {
            ItemType.BOOK -> {
                val pages = field2.toIntOrNull()
                if (pages == null || pages <= 0) {
                    _errorMessage.value = "Введите корректное количество страниц"
                    return
                }
                if (name.isBlank() || field1.isBlank()) {
                    _errorMessage.value = "Все поля должны быть заполнены"
                    return
                }
                _createdItem.value = Book(
                    id = id,
                    isAvailable = true,
                    name = name,
                    pages = pages,
                    author = field1
                )
            }
            ItemType.DISK -> {
                if (name.isBlank() || field1.isBlank()) {
                    _errorMessage.value = "Все поля должны быть заполнены"
                    return
                }
                _createdItem.value = Disk(
                    id = id,
                    isAvailable = true,
                    name = name,
                    type = field1
                )
            }
            ItemType.NEWSPAPER -> {
                val issueNumber = field1.toIntOrNull()
                if (issueNumber == null || issueNumber <= 0) {
                    _errorMessage.value = "Введите корректный номер выпуска"
                    return
                }
                val month = Month.entries.firstOrNull {
                    it.russianMonth.equals(field2.trim(), ignoreCase = true)
                }
                if (month == null) {
                    _errorMessage.value = "Введите корректное название месяца"
                    return
                }
                if (name.isBlank()) {
                    _errorMessage.value = "Название не должно быть пустым"
                    return
                }
                _createdItem.value = Newspaper(
                    id = id,
                    isAvailable = true,
                    name = name,
                    issueNumber = issueNumber,
                    month = month
                )
            }
        }
        _errorMessage.value = null // Очистить ошибку, если всё прошло хорошо
    }
}


data class FormState(
    val type: ItemType = ItemType.BOOK,
    val name: String = "",
    val id: Int = 0,
    val field1: String = "",
    val field2: String = ""
)