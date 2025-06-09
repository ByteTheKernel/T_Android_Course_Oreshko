package com.example.tapplication.domain.usecases

import com.example.tapplication.common.utils.SortOrder
import com.example.tapplication.domain.repositories.SettingsRepository
import javax.inject.Inject

class SaveSortOrderUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(sortOrder: SortOrder) {
        settingsRepository.saveSortOrders(sortOrder)
    }
}