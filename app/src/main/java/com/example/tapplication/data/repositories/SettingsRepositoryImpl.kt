package com.example.tapplication.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.example.tapplication.common.utils.SortOrder
import com.example.tapplication.domain.repositories.SettingsRepository

class SettingsRepositoryImpl(context: Context): SettingsRepository {

    companion object {
        private const val PREFS_NAME = "prefs"
        private const val KEY_SORT_ORDER = "sortOrder"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveSortOrders(order: SortOrder) {
        prefs.edit() { putString(KEY_SORT_ORDER, order.name) }
    }

    override fun getSortOrder(): SortOrder {
        val value = prefs.getString(KEY_SORT_ORDER, SortOrder.CREATED_AT.name) ?: SortOrder.CREATED_AT.name
        return SortOrder.Companion.fromString(value)
    }
}