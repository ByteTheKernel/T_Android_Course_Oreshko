package com.example.tapplication.shop

import com.example.tapplication.library.LibraryItem

interface Shop<out T: LibraryItem> {
    fun sell(): T
}