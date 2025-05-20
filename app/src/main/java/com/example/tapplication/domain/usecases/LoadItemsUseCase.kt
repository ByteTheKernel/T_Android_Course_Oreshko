package com.example.tapplication.domain.usecases

import com.example.tapplication.common.utils.SortOrder
import com.example.tapplication.domain.entities.LibraryItem
import com.example.tapplication.domain.repositories.LibraryRepository
import javax.inject.Inject

class LoadItemsUseCase @Inject constructor(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(
        page: Int = 0,
        sortOrder: SortOrder = SortOrder.CREATED_AT
    ): List<LibraryItem> {
        return libraryRepository.getData(page, sortOrder)
    }
}