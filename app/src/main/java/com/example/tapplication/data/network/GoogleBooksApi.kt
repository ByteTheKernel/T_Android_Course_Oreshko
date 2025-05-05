package com.example.tapplication.data.network

import com.example.tapplication.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("fields") fields: String = "items(volumeInfo/title,volumeInfo/authors,volumeInfo/pageCount,volumeInfo/industryIdentifiers,volumeInfo/imageLinks)",
        @Query("key") apiKey: String = BuildConfig.GOOGLE_BOOKS_API_KEY
    ): GoogleBooksResponse
}