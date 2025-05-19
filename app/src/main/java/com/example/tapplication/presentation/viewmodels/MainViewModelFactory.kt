package com.example.tapplication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tapplication.data.repositories.LibraryRepositoryImpl
import com.example.tapplication.data.repositories.RemoteBooksRepositoryImpl
import com.example.tapplication.data.repositories.SettingsRepositoryImpl
import com.example.tapplication.domain.usecases.*

class MainViewModelFactory(
    private val libraryRepository: LibraryRepositoryImpl,
    private val remoteBooksRepository: RemoteBooksRepositoryImpl,
    private val settingsRepository: SettingsRepositoryImpl
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {

            val loadItemsUseCase = LoadItemsUseCase(libraryRepository)
            val addItemUseCase = AddItemUseCase(libraryRepository)
            val updateItemStatusUseCase = UpdateItemStatusUseCase(libraryRepository)
            val removeItemUseCase = RemoveItemUseCase(libraryRepository)
            val searchOnlineUseCase = SearchOnlineUseCase(remoteBooksRepository)
            val saveSortOrderUseCase = SaveSortOrderUseCase(settingsRepository)
            val getSavedSortOrderUseCase = GetSavedSortOrderUseCase(settingsRepository)

            return MainViewModel(
                loadItemsUseCase,
                addItemUseCase,
                updateItemStatusUseCase,
                removeItemUseCase,
                searchOnlineUseCase,
                saveSortOrderUseCase,
                getSavedSortOrderUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}