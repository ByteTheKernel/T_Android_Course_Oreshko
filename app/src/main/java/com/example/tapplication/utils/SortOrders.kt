package com.example.tapplication.utils

import com.example.tapplication.R

enum class SortOrder(val displayNameRes: Int) {
    NAME(R.string.sort_by_name),
    CREATED_AT(R.string.sort_by_created_at);

    companion object {
        fun fromSpinnerPosition(position: Int): SortOrder {
            return entries.getOrElse(position) { CREATED_AT }
        }

        fun toSpinnerPosition(order: SortOrder): Int {
            return order.ordinal
        }

        fun fromString(value: String): SortOrder {
            val sortOrder = try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                CREATED_AT
            }
            return sortOrder
        }
    }
}