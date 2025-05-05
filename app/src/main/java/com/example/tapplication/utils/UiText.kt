package com.example.tapplication.utils

import android.content.Context

sealed class UiText {
    data class DynamicString(val value: String): UiText()
    data class StringResource(val resId: Int, val args: List<Any> = emptyList()): UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args.toTypedArray())
        }
    }

    companion object {
        fun from(resId: Int, vararg args: Any): UiText {
            return StringResource(resId, args.toList())
        }
        fun from(value: String): UiText {
            return DynamicString(value)
        }
    }
}