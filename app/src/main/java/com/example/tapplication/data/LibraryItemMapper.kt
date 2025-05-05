package com.example.tapplication.data

import com.example.tapplication.library.*
import com.example.tapplication.utils.*

object LibraryItemMapper {
    fun fromEntity(entity: LibraryItemEntity): LibraryItem {
        return when (entity.type) {
            ItemType.BOOK.name -> Book(
                id = entity.id,
                isAvailable = entity.isAvailable,
                name = entity.name,
                pages = entity.pages ?: 0,
                author = entity.author.orEmpty()
            )
            ItemType.NEWSPAPER.name -> Newspaper(
                id = entity.id,
                isAvailable = entity.isAvailable,
                name = entity.name,
                issueNumber = entity.issueNumber ?: 0,
                month = entity.month?.let { Month.valueOf(it) } ?: Month.JANUARY
            )
            ItemType.DISK.name -> Disk(
                id = entity.id,
                isAvailable = entity.isAvailable,
                name = entity.name,
                type = entity.diskType.orEmpty()
            )
            else -> throw IllegalArgumentException("Unknown item type: ${entity.type}")
        }
    }

    fun toEntity(item: LibraryItem): LibraryItemEntity {
        return when (item) {
            is Book -> LibraryItemEntity(
                id = item.id,
                type = ItemType.BOOK.name,
                name = item.name,
                isAvailable = item.isAvailable,
                pages = item.pages,
                author = item.author,
                issueNumber = null,
                month = null,
                diskType = null,
                createdAt = System.currentTimeMillis()
            )
            is Newspaper -> LibraryItemEntity(
                id = item.id,
                type = ItemType.NEWSPAPER.name,
                name = item.name,
                isAvailable = item.isAvailable,
                pages = null,
                author = null,
                issueNumber = item.issueNumber,
                month = item.month.name,
                diskType = null,
                createdAt = System.currentTimeMillis()
            )
            is Disk -> LibraryItemEntity(
                id = item.id,
                type = ItemType.DISK.name,
                name = item.name,
                isAvailable = item.isAvailable,
                pages = null,
                author = null,
                issueNumber = null,
                month = null,
                diskType = item.type,
                createdAt = System.currentTimeMillis()
            )
            else -> throw IllegalArgumentException("Unknown item class: ${item::class}")
        }
    }
}