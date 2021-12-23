package com.example.fitnessfactory_client.di

import com.example.fitnessfactory_client.ui.AuthActivityViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@AppScope
@Singleton
interface AppComponent {

    fun inject(authActivityViewModelFactory: AuthActivityViewModelFactory)
}