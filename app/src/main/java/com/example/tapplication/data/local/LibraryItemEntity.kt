package com.example.tapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library_items")
data class LibraryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val name: String,
    val isAvailable: Boolean,
    val pages: Int?,
    val author: String?,
    val issueNumber: Int?,
    val month: String?,
    val diskType: String?,
    val createdAt: Long
)