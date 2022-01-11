package com.example.fitnessfactory_client.ui.screens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfactory_client.FFApp
import javax.inject.Inject

class HomeScreenViewModelFactory: ViewModelProvider.Factory {

    @Inject
    lateinit var homeScreenViewModel: HomeScreenViewModel

    init {
        FFApp.instance.appComponent.inject(this)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return homeScreenViewModel as T
    }
}