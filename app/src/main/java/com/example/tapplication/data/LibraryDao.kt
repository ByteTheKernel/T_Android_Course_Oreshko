package com.example.tapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    @Query("SELECT * FROM library_items ORDER BY name ASC LIMIT :limit OFFSET :offset")
    fun getItemsByName(limit: Int, offset: Int): List<LibraryItemEntity>

    @Query("SELECT * FROM library_items ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    fun getItemsByCreatedAt(limit: Int, offset: Int): List<LibraryItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LibraryItemEntity)

    @Delete
    suspend fun delete(item: LibraryItemEntity)

    @Query("DELETE FROM library_items WHERE id = :itemId")
    suspend fun deleteById(itemId: Int)

    @Query("SELECT COUNT(*) FROM library_items")
    suspend fun getItemCount(): Int
}