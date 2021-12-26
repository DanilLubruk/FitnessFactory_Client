package com.example.fitnessfactory_client.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfactory_client.FFApp
import javax.inject.Inject

class AuthActivityViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var authViewModel: AuthActivityViewModel

    init {
        FFApp.instance.appComponent.inject(this)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return authViewModel as T
    }
}