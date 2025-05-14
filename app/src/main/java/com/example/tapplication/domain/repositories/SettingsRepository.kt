package com.example.tapplication.domain.repositories

import com.example.tapplication.common.utils.SortOrder

interface SettingsRepository {
    fun saveSortOrders(order: SortOrder)
    fun getSortOrder(): SortOrder
}