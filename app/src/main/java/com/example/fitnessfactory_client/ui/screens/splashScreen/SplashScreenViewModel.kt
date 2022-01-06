package com.example.fitnessfactory_client.ui.screens.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import com.example.fitnessfactory_client.utils.GuiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenViewModel
@Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager) :
    ViewModel() {

    private val mutableIsAuthState = MutableSharedFlow<IsAuthState>()
    val isAuthState: SharedFlow<IsAuthState> = mutableIsAuthState

    fun checkLoggedIn() {
        viewModelScope.launch {
            firebaseAuthManager.isLoggedInFlow()
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                    mutableIsAuthState.emit(IsAuthState.Error(throwable = throwable))
                }
                .collect { isLoggedIn ->
                    val authState =
                        if (isLoggedIn)
                            IsAuthState.LoggedIn()
                        else
                            IsAuthState.LoggedOut()

                    mutableIsAuthState.emit(authState)
                }
        }
    }
}