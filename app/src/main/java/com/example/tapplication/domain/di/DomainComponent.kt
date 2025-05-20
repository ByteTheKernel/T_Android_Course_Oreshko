package com.example.tapplication.domain.di

import com.example.tapplication.domain.usecases.AddItemUseCase
import com.example.tapplication.domain.usecases.GetSavedSortOrderUseCase
import com.example.tapplication.domain.usecases.LoadItemsUseCase
import com.example.tapplication.domain.usecases.RemoveItemUseCase
import com.example.tapplication.domain.usecases.SaveSortOrderUseCase
import com.example.tapplication.domain.usecases.SearchOnlineUseCase
import com.example.tapplication.domain.usecases.UpdateItemStatusUseCase
import dagger.Subcomponent

@Subcomponent(modules = [DomainModule::class])
interface DomainComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): DomainComponent
    }

    fun inject(useCase: LoadItemsUseCase)
    fun inject(useCase: AddItemUseCase)
    fun inject(useCase: RemoveItemUseCase)
    fun inject(useCase: UpdateItemStatusUseCase)
    fun inject(useCase: SaveSortOrderUseCase)
    fun inject(useCase: GetSavedSortOrderUseCase)
    fun inject(useCase: SearchOnlineUseCase)
}