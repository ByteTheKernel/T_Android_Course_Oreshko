package com.example.tapplication.data

import android.content.Context
import com.example.tapplication.utils.SortOrder
import androidx.core.content.edit

class SettingsRepository(context: Context) {

    companion object {
        private const val PREFS_NAME = "prefs"
        private const val KEY_SORT_ORDER = "sortOrder"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSortOrders(order: SortOrder) {
        prefs.edit() { putString(KEY_SORT_ORDER, order.name) }
    }

    fun getSortOrder(): SortOrder {
        val value = prefs.getString(KEY_SORT_ORDER, SortOrder.CREATED_AT.name) ?: SortOrder.CREATED_AT.name
        return SortOrder.fromString(value)
    }

}