package com.example.fitnessfactory_client.ui

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import javax.inject.Inject

class AuthActivityViewModel
@Inject constructor(private val firebaseAuthManager: FirebaseAuthManager) : ViewModel() {

    val signInIntent: LiveData<Intent> = liveData {
        val data = firebaseAuthManager.getSignInIntent()
        emit(data)
    }
}