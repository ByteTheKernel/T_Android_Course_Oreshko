package com.example.tapplication.domain.usecases

import com.example.tapplication.domain.repositories.LibraryRepository
import javax.inject.Inject

class RemoveItemUseCase @Inject constructor(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(itemId: Int): Boolean {
        return libraryRepository.removeItem(itemId)
    }
}