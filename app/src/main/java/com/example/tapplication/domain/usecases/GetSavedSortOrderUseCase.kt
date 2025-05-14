package com.example.tapplication.domain.usecases

import com.example.tapplication.common.utils.SortOrder
import com.example.tapplication.domain.repositories.SettingsRepository

class GetSavedSortOrderUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): SortOrder {
        return settingsRepository.getSortOrder()
    }
}