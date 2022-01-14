package com.example.fitnessfactory_client.ui.screens.appScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.CoachData
import com.example.fitnessfactory_client.data.beans.GymsChainData
import com.example.fitnessfactory_client.data.managers.GymsChainDataManager
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import com.example.fitnessfactory_client.ui.screens.splashScreen.IsAuthState
import com.example.fitnessfactory_client.utils.GuiUtils
import com.example.fitnessfactory_client.utils.ResUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppScreenViewModel
@Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager
) : ViewModel() {

    private val mutableLogoutState = MutableSharedFlow<LogoutState>()
    val logoutState: SharedFlow<LogoutState> = mutableLogoutState

    fun logout() {
        viewModelScope.launch {
            firebaseAuthManager.signOutFlow()
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    mutableLogoutState.emit(LogoutState.Failure(throwable.localizedMessage))
                }
                .collect { isLoggedOut ->
                    val logoutState =
                        if (isLoggedOut)
                            LogoutState.Success()
                        else
                            LogoutState.Failure(ResUtils.getString(R.string.message_error_signout))

                    mutableLogoutState.emit(logoutState)
                }
        }
    }
}