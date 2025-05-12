package com.example.tapplication.data.datasources.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tapplication.data.models.LibraryItemEntity

@Dao
interface LibraryDao {
    @Query("SELECT * FROM library_items ORDER BY name ASC LIMIT :limit OFFSET :offset")
    fun getItemsByName(limit: Int, offset: Int): List<LibraryItemEntity>

    @Query("SELECT * FROM library_items ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    fun getItemsByCreatedAt(limit: Int, offset: Int): List<LibraryItemEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(item: LibraryItemEntity)

    @Delete
    suspend fun delete(item: LibraryItemEntity)

    @Query("DELETE FROM library_items WHERE id = :itemId")
    suspend fun deleteById(itemId: Int)

    @Query("SELECT COUNT(*) FROM library_items")
    suspend fun getItemCount(): Int
}