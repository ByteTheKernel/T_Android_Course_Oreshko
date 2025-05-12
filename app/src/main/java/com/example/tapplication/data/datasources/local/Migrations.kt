package com.example.tapplication.data.datasources.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE library_items_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                type TEXT NOT NULL,
                name TEXT NOT NULL,
                isAvailable INTEGER NOT NULL,
                pages INTEGER,
                author TEXT,
                issueNumber INTEGER,
                month TEXT,
                diskType TEXT,
                createdAt INTEGER NOT NULL
            )
        """.trimIndent())

        val cursor = database.query("SELECT * FROM library_items")
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val isAvailable = cursor.getInt(cursor.getColumnIndexOrThrow("isAvailable"))
            val createdAt = cursor.getLong(cursor.getColumnIndexOrThrow("createdAt"))
            val details = cursor.getString(cursor.getColumnIndexOrThrow("details")) ?: ""

            var pages: Int? = null
            var author: String? = null
            var issueNumber: Int? = null
            var month: String? = null
            var diskType: String? = null

            when (type) {
                "BOOK" -> {
                    val parts = details.split("|")
                    pages = parts.getOrNull(0)?.toIntOrNull()
                    author = parts.getOrNull(1)
                }
                "NEWSPAPER" -> {
                    val parts = details.split("|")
                    issueNumber = parts.getOrNull(0)?.toIntOrNull()
                    month = parts.getOrNull(1)
                }
                "DISK" -> {
                    diskType = details
                }
            }

            database.execSQL("""
                INSERT INTO library_items_new (
                    id, type, name, isAvailable, pages, author, issueNumber, month, diskType, createdAt
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, arrayOf(id, type, name, isAvailable, pages, author, issueNumber, month, diskType, createdAt))
        }
        cursor.close()

        database.execSQL("DROP TABLE library_items")
        database.execSQL("ALTER TABLE library_items_new RENAME TO library_items")
    }
}
