package com.example.tapplication.di

import android.app.Application
import android.content.Context
import com.example.tapplication.data.datasources.local.AppDatabase
import com.example.tapplication.data.datasources.local.LibraryDao
import com.example.tapplication.data.datasources.network.GoogleBooksApi
import com.example.tapplication.data.datasources.network.RetrofitHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
        AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideLibraryDao(appDatabase: AppDatabase): LibraryDao =
        appDatabase.libraryDao()

    @Provides
    @Singleton
    fun provideRetrofit(): GoogleBooksApi =
        RetrofitHelper.createRetrofit()
}