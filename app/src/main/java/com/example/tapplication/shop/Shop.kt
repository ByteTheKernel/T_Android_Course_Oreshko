package com.example.tapplication.shop

import com.example.tapplication.library.LibraryItem

interface Shop<T: LibraryItem> {
    fun sell(): T
}