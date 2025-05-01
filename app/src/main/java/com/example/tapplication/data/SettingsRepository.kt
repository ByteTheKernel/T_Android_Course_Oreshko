package com.example.tapplication.data

import android.content.Context
import com.example.tapplication.utils.SortOrder
import androidx.core.content.edit

class SettingsRepository(context: Context) {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun saveSortOrders(order: SortOrder) {
        prefs.edit() { putString("sortOrder", order.name) }
    }

    fun getSortOrder(): SortOrder {
        val value = prefs.getString("sortOrder", SortOrder.CREATED_AT.name) ?: SortOrder.CREATED_AT.name
        return SortOrder.fromString(value)
    }

}