package com.example.fitnessfactory_client.ui.screens.gymsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfactory_client.FFApp
import javax.inject.Inject

class GymsScreenViewModelFactory: ViewModelProvider.Factory {

    @Inject
    lateinit var viewModel: GymsScreenViewModel

    init {
        FFApp.instance.appComponent.inject(this)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}