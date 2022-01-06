package com.example.fitnessfactory_client.ui.screens.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfactory_client.FFApp
import javax.inject.Inject

class SplashScreenViewModelFactory: ViewModelProvider.Factory {

    @Inject
    lateinit var splashViewModel: SplashScreenViewModel

    init {
        FFApp.instance.appComponent.inject(this)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return splashViewModel as T
    }
}