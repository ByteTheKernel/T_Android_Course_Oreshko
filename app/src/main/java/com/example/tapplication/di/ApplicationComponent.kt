package com.example.tapplication.di

import com.example.tapplication.data.di.DataComponent
import com.example.tapplication.data.di.DataModule
import com.example.tapplication.domain.di.DomainComponent
import com.example.tapplication.domain.di.DomainModule
import com.example.tapplication.presentation.MainActivity
import com.example.tapplication.presentation.di.DaggerViewModelFactory
import com.example.tapplication.presentation.di.PresentationComponent
import com.example.tapplication.presentation.di.PresentationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DataModule::class,
    PresentationModule::class
])
interface ApplicationComponent {

    fun dataComponent(): DataComponent.Factory
    fun domainComponent(): DomainComponent.Factory
    fun presentationComponent(): PresentationComponent.Factory
}