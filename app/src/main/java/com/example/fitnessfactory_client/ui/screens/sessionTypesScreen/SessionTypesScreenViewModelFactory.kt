package com.example.fitnessfactory_client.ui.screens.sessionTypesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfactory_client.FFApp
import javax.inject.Inject

class SessionTypesScreenViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var viewModel: SessionTypesScreenViewModel

    init {
        FFApp.instance.appComponent.inject(this)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}