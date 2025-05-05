package com.example.tapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tapplication.data.repository.LibraryRepository
import com.example.tapplication.data.repository.RemoteBooksRepository
import com.example.tapplication.data.repository.SettingsRepository

class MainViewModelFactory(
    private val libraryRepository: LibraryRepository,
    private val remoteBooksRepository: RemoteBooksRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(libraryRepository, remoteBooksRepository, settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}