package com.example.tapplication

import android.app.Application
import com.example.tapplication.data.di.DataComponent
import com.example.tapplication.di.AppModule
import com.example.tapplication.di.ApplicationComponent
import com.example.tapplication.di.DaggerApplicationComponent
import com.example.tapplication.domain.di.DomainComponent
import com.example.tapplication.presentation.di.PresentationComponent


class App: Application() {
    lateinit var appComponent: ApplicationComponent
    lateinit var dataComponent: DataComponent
    lateinit var domainComponent: DomainComponent
    lateinit var presentationComponent: PresentationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()

        dataComponent = appComponent.dataComponent().create()
        domainComponent = appComponent.domainComponent().create()
        presentationComponent = appComponent.presentationComponent().create()
    }
}