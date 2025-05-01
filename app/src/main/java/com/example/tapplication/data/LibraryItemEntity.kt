package com.example.tapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library_items")
data class LibraryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val name: String,
    val isAvailable: Boolean,
    val details: String,
    val createdAt: Long
)