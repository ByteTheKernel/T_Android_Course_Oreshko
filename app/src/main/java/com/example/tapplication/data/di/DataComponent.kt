package com.example.tapplication.data.di

import com.example.tapplication.data.repositories.LibraryRepositoryImpl
import com.example.tapplication.data.repositories.RemoteBooksRepositoryImpl
import com.example.tapplication.data.repositories.SettingsRepositoryImpl
import com.example.tapplication.domain.repositories.LibraryRepository
import com.example.tapplication.domain.repositories.RemoteBooksRepository
import com.example.tapplication.domain.repositories.SettingsRepository
import dagger.Subcomponent

@Subcomponent(modules = [DataModule::class])
interface DataComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): DataComponent
    }

    fun inject(repository: LibraryRepositoryImpl)
    fun inject(repository: RemoteBooksRepositoryImpl)
    fun inject(repository: SettingsRepositoryImpl)
}