package com.example.tapplication.domain.di

import com.example.tapplication.domain.repositories.LibraryRepository
import com.example.tapplication.domain.repositories.RemoteBooksRepository
import com.example.tapplication.domain.repositories.SettingsRepository
import com.example.tapplication.domain.usecases.AddItemUseCase
import com.example.tapplication.domain.usecases.GetSavedSortOrderUseCase
import com.example.tapplication.domain.usecases.LoadItemsUseCase
import com.example.tapplication.domain.usecases.RemoveItemUseCase
import com.example.tapplication.domain.usecases.SaveSortOrderUseCase
import com.example.tapplication.domain.usecases.SearchOnlineUseCase
import com.example.tapplication.domain.usecases.UpdateItemStatusUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideLoadItemsUseCase(repo: LibraryRepository): LoadItemsUseCase =
        LoadItemsUseCase(repo)

    @Provides
    @Singleton
    fun provideAddItemUseCase(repo: LibraryRepository): AddItemUseCase =
        AddItemUseCase(repo)

    @Provides
    @Singleton
    fun provideRemoveItemUseCase(repo: LibraryRepository): RemoveItemUseCase =
        RemoveItemUseCase(repo)

    @Provides
    @Singleton
    fun provideUpdateItemStatusUseCase(repo: LibraryRepository): UpdateItemStatusUseCase =
        UpdateItemStatusUseCase(repo)

    @Provides
    @Singleton
    fun provideSaveSortOrderUseCase(repo: SettingsRepository): SaveSortOrderUseCase =
        SaveSortOrderUseCase(repo)

    @Provides
    @Singleton
    fun provideGetSavedSortOrderUseCase(repo: SettingsRepository): GetSavedSortOrderUseCase =
        GetSavedSortOrderUseCase(repo)

    @Provides
    @Singleton
    fun provideSearchOnlineUseCase(repo: RemoteBooksRepository): SearchOnlineUseCase =
        SearchOnlineUseCase(repo)
}