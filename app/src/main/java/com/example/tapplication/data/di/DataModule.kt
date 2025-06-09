package com.example.tapplication.data.di

import android.content.Context
import com.example.tapplication.data.datasources.local.AppDatabase
import com.example.tapplication.data.datasources.local.LibraryDao
import com.example.tapplication.data.datasources.network.GoogleBooksApi
import com.example.tapplication.data.repositories.LibraryRepositoryImpl
import com.example.tapplication.data.repositories.RemoteBooksRepositoryImpl
import com.example.tapplication.data.repositories.SettingsRepositoryImpl
import com.example.tapplication.domain.repositories.LibraryRepository
import com.example.tapplication.domain.repositories.RemoteBooksRepository
import com.example.tapplication.domain.repositories.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Binds
    abstract fun bindLibraryRepository(impl: LibraryRepositoryImpl): LibraryRepository

    @Binds
    abstract fun bindRemoteBooksRepository(impl: RemoteBooksRepositoryImpl): RemoteBooksRepository

    @Binds
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}