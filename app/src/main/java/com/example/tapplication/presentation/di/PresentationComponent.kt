package com.example.tapplication.presentation.di

import com.example.tapplication.presentation.MainActivity
import com.example.tapplication.presentation.fragments.add.AddFragment
import com.example.tapplication.presentation.fragments.detail.DetailFragment
import com.example.tapplication.presentation.fragments.list.ListFragment
import com.example.tapplication.presentation.viewmodels.DetailsViewModel
import com.example.tapplication.presentation.viewmodels.MainViewModel
import dagger.Subcomponent

@Subcomponent(modules = [PresentationModule::class])
interface PresentationComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PresentationComponent
    }

    fun daggerViewModelFactory(): DaggerViewModelFactory

    fun inject(fragment: ListFragment)
    fun inject(fragment: DetailFragment)
    fun inject(fragment: AddFragment)
    fun inject(activity: MainActivity)
}