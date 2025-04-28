package com.example.tapplication.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tapplication.R
import com.example.tapplication.data.LibraryRepository
import com.example.tapplication.library.LibraryItem
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = LibraryRepository()

    val itemsFlow: StateFlow<List<LibraryItem>> = repository.itemsFlow

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
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.addItem(newItem)
                }
                _toastMessage.value = getApplication<Application>().getString(
                    R.string.message_item_added
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _toastMessage.value = getApplication<Application>().getString(
                    R.string.error_adding_item
                )
            }
        }
    }

    fun updateItemAvailability(item: LibraryItem) {
        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "Attempting to update item availability: ${item.id}")
                val updateItem = withContext(Dispatchers.IO) {
                    repository.updateItemAvailability(item)
                }
                if (updateItem != null) {
                    _toastMessage.value = getApplication<Application>().getString(
                        R.string.message_item_status_updated,
                        item.id
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _toastMessage.value = getApplication<Application>().getString(
                    R.string.error_updating_item_status
                )
            }
        }
    }

    fun onToastShown() {
        _toastMessage.value = null
    }

    fun removeItem(position: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.removeItem(position)
                }
            } catch (e: CancellationException){
                throw e
            } catch (e: Exception) {
                _toastMessage.value = getApplication<Application>().getString(
                    R.string.error_removing_item
                )
            }
        }
    }

    fun getItemById(itemId: Int): LibraryItem? {
        return itemsFlow.value.find { it.id == itemId }
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
                withContext(Dispatchers.IO) {
                    repository.initializeIfNeeded()
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = getApplication<Application>().getString(
                    R.string.error_loading_items
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}