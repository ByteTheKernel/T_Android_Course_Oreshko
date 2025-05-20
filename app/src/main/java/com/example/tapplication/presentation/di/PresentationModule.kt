package com.example.tapplication.presentation.di

import androidx.lifecycle.ViewModel
import com.example.tapplication.presentation.viewmodels.DetailsViewModel
import com.example.tapplication.presentation.viewmodels.MainViewModel
import dagger.Binds
import dagger.multibindings.IntoMap
import dagger.Module

@Module
abstract class PresentationModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindDetailsViewModel(viewModel: DetailsViewModel): ViewModel
}