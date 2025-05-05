package com.example.tapplication.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tapplication.R
import com.example.tapplication.data.repository.LibraryRepository
import com.example.tapplication.data.repository.RemoteBooksRepository
import com.example.tapplication.data.repository.SettingsRepository
import com.example.tapplication.library.LibraryItem
import com.example.tapplication.utils.SortOrder
import com.example.tapplication.utils.UiText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.PI

class MainViewModel(
    private val libraryRepository: LibraryRepository,
    private val remoteBooksRepository: RemoteBooksRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {

    enum class Tab { LIBRARY, GOOGLE_BOOKS }

    private val _selectedTab = MutableLiveData(Tab.LIBRARY)
    val selectedTab: LiveData<Tab> = _selectedTab

    private val _items = MutableLiveData<List<LibraryItem>>()
    val items: LiveData<List<LibraryItem>> = _items

    private val _selectedItem = MutableLiveData<LibraryItem?>()
    val selectedItem: LiveData<LibraryItem?> = _selectedItem

    private val _selectedItemId = MutableLiveData<Int?>()
    val selectedItemId: LiveData<Int?> = _selectedItemId

    private val _toastMessage = MutableLiveData<UiText?>()
    val toastMessage: LiveData<UiText?> = _toastMessage

    private val _error = MutableLiveData<UiText?>()
    val error: LiveData<UiText?> = _error

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isScrollLoading = MutableLiveData(false)
    val isScrollLoading: LiveData<Boolean> = _isScrollLoading

    private val _initialLoadDone = MutableLiveData<Boolean>(false)
    val initialLoadDone: LiveData<Boolean> = _initialLoadDone

    private val _scrollPosition = MutableLiveData<Int>()
    val scrollPosition: LiveData<Int> = _scrollPosition

    private val pageSize = 30
    private var currentPage = 0
    private var currentSortOrder = getSavedSortOrder()

    init {
        loadData(currentPage, currentSortOrder)
    }

    fun selectTab(tab: Tab) {
        if (_selectedTab.value == tab) return

        _selectedTab.value = tab
        _items.value = emptyList()
        _scrollPosition.value = 0

        if (tab == Tab.LIBRARY) {
            currentPage = 0
            loadData(currentPage, currentSortOrder)
        }
    }

    fun loadItemsForScroll(firstVisibleItemPosition: Int, lastVisibleItemPosition: Int) {
        viewModelScope.launch {
            try {
                _isScrollLoading.value = true

                val currentTab = _selectedTab.value

                when(currentTab) {
                    Tab.LIBRARY -> {
                        if (lastVisibleItemPosition >= (_items.value?.size ?: 0) - 10) {
                            loadNextPage()
                        }

                        if (firstVisibleItemPosition <= 10 && currentPage > 0) {
                            loadPreviousPage()
                        }
                    }
                    else -> {}
                }
            } catch (e: CancellationException) {
              throw e
            } catch (e: Exception) {
                _error.value = UiText.from(
                    R.string.error_loading_items
                )
            } finally {
                _isScrollLoading.value = false
            }
        }
    }

    fun addItem(newItem: LibraryItem) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    libraryRepository.addItem(newItem)
                }
                _toastMessage.value = UiText.from(R.string.message_item_added)
                loadData()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _toastMessage.value = UiText.from(R.string.error_adding_item)
            }
        }
    }

    fun updateItemAvailability(item: LibraryItem) {
        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "Attempting to update item availability: ${item.id}")
                val updateItem = withContext(Dispatchers.IO) {
                    libraryRepository.updateItemAvailability(item)
                }
                if (updateItem != null) {
                    val currentItems = _items.value.orEmpty().toMutableList()
                    val index = currentItems.indexOfFirst { it.id == updateItem.id }
                    if (index != -1) {
                        currentItems[index] = updateItem
                        _items.postValue(currentItems)
                    }
                }

                _toastMessage.value = UiText.from(
                    R.string.message_item_status_updated,
                    item.id
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _toastMessage.value = UiText.from(
                    R.string.error_updating_item_status
                )
            }
        }
    }

    fun onToastShown() {
        _toastMessage.value = null
    }

    fun removeItem(itemId: Int) {
        viewModelScope.launch {
            try {
                val remove = withContext(Dispatchers.IO) {
                    libraryRepository.removeItem(itemId)
                }
                if (remove) {
                    val currentItems = _items.value.orEmpty().toMutableList()
                    val itemToRemove = currentItems.find { it.id == itemId }
                    if (itemToRemove != null) {
                        currentItems.remove(itemToRemove)
                        _items.postValue(currentItems)
                    }
                }

                _toastMessage.value = UiText.from(
                    R.string.message_item_removed
                )
            } catch (e: CancellationException){
                throw e
            } catch (e: Exception) {
                _toastMessage.value = UiText.from(
                    R.string.error_removing_item
                )
            }
        }
    }

    fun setSortOrder(newSortOrder: SortOrder) {
        if (newSortOrder == currentSortOrder) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                currentSortOrder = newSortOrder
                currentPage = 0
                settingsRepository.saveSortOrders(newSortOrder)

                val items = withContext(Dispatchers.IO) {
                    libraryRepository.getData(currentPage, currentSortOrder)
                }
                _items.value = items
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.d("MainViewModel", "SortOrder exception", e)
                _error.value = UiText.from(
                    R.string.error_applying_sorting
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadNextPage() {
        val nextPage = currentPage++
        val nextPageItems = withContext(Dispatchers.IO) {
            libraryRepository.getData(nextPage, currentSortOrder)
        }
        val currentItems = _items.value.orEmpty()

        val itemsToRemove = minOf(nextPageItems.size, pageSize / 2)

        val updatedItems = currentItems.drop(itemsToRemove) + nextPageItems.take(itemsToRemove)

        _items.value = updatedItems
    }

    private suspend fun loadPreviousPage() {
        val previousPage = currentPage--
        if(previousPage < 0) return

        val previousPageItems = withContext(Dispatchers.IO) {
            libraryRepository.getData(previousPage, currentSortOrder)
        }
        val currentItems = _items.value.orEmpty()

        val itemsToRemove = minOf(previousPageItems.size, pageSize / 2)

        val updatedItems = previousPageItems.take(itemsToRemove) + currentItems.take(currentItems.size - itemsToRemove)

        _items.value = updatedItems
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

    internal fun loadData(page: Int = 0, sortOrder: SortOrder = currentSortOrder) {
        viewModelScope.launch {
            Log.d("MainViewModel", "Starting data loading...")
            _isLoading.value = true
            try {
                val items = withContext(Dispatchers.IO) {
                    Log.d("MainViewModel", "Calling repository.loadInitialData")
                    libraryRepository.getData(page, sortOrder)
                }
                _items.value = items
                _initialLoadDone.value = true
                Log.d("MainViewModel", "_items.value = items: ${_items.value?.size}")
            } catch (e: CancellationException) {
                Log.e("MainViewModel", "Coroutine cancelled", e)
                throw e
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error during data loading", e)
                _error.value = UiText.from(
                    R.string.error_loading_items
                )
            } finally {
                Log.d("MainViewModel", "Finally block reached")
                _isLoading.value = false
            }
        }
    }

    fun searchBooksOnline(title: String?, author: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = withContext(Dispatchers.IO) {
                    remoteBooksRepository.searchBooksOnline(title, author)
                }
                Log.d("MainViewModel:searchBooksOnline", "results: $results")
                _items.value = results
                Log.d("MainViewModel:searchBooksOnline", "_items.value = items: ${_items.value?.size}")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = UiText.from(
                    R.string.error_loading_items
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveGoogleBookToLibrary(item: LibraryItem) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    libraryRepository.addItem(item)
                }
                _toastMessage.value = UiText.from(R.string.message_item_added)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _toastMessage.value = UiText.from(R.string.error_adding_item)
            }
        }
    }


    fun getSavedSortOrder(): SortOrder {
        return settingsRepository.getSortOrder()
    }
}