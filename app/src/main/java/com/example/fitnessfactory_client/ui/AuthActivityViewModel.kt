package com.example.fitnessfactory_client.ui

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager

class AuthActivityViewModel(firebaseAuthManager: FirebaseAuthManager): ViewModel() {

    val signInIntent: LiveData<Intent> = liveData {
        val data = firebaseAuthManager.getSignInIntent()
        emit(data)
    }
}