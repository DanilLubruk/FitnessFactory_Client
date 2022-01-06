package com.example.fitnessfactory_client.ui.screens.appScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfactory_client.FFApp
import javax.inject.Inject

class AppScreenViewModelFactory: ViewModelProvider.Factory {

    @Inject
    lateinit var appScreenViewModel: AppScreenViewModel

    init {
        FFApp.instance.appComponent.inject(this)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return appScreenViewModel as T
    }
}