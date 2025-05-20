package com.example.tapplication.domain.usecases

import com.example.tapplication.domain.entities.LibraryItem
import com.example.tapplication.domain.repositories.LibraryRepository
import javax.inject.Inject

class AddItemUseCase @Inject constructor(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(item: LibraryItem) {
        libraryRepository.addItem(item)
    }
}