package com.example.tapplication.library

interface LibraryItem {
    val id: Int
    var isAvailable: Boolean
    val name: String

    fun getShortInfo(): String
    fun getDetailedInfo(): String
}