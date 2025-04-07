package com.example.tapplication.management

import com.example.tapplication.library.Disk
import com.example.tapplication.library.LibraryItem

interface Digitalizable<in T: LibraryItem> {
    fun toDisk(newId: Int): Disk
}