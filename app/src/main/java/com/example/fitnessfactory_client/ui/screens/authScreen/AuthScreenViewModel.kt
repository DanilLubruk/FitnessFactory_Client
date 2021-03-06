package com.example.fitnessfactory_client.ui.screens.authScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.managers.AuthManager
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import com.example.fitnessfactory_client.utils.GuiUtils
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthScreenViewModel
@Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager,
    private val authManager: AuthManager,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val registerUiStateChannel: Channel<RegisterUiState> = Channel()
    fun getRegisterUiState(): Flow<RegisterUiState> =
        registerUiStateChannel.consumeAsFlow()

    private val ownersDataChannel: Channel<PickOwnerUiState> = Channel()
    fun getOwnersData(): Flow<PickOwnerUiState> =
        ownersDataChannel.consumeAsFlow()

    fun registerClient(ownerId: String) {
        viewModelScope.launch {
            authManager.registerClient(ownerId = ownerId)
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                }
                .collect()
        }
    }

    fun registerUser(googleSignInAccount: GoogleSignInAccount) {
        viewModelScope.launch {
            authManager.handleSignInResult(googleSignInAccount = googleSignInAccount)
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                    registerUiStateChannel.send(RegisterUiState.Error(throwable))
                }
                .collect {
                    registerUiStateChannel.send(RegisterUiState.Success(usersEmail = it.email))
                }
        }
    }

    fun getOwnersData(usersEmail: String) {
        viewModelScope.launch {
           authManager.getAuthOwnersData(usersEmail = usersEmail)
               .flowOn(Dispatchers.IO)
               .catch { throwable ->
                   throwable.printStackTrace()
                   GuiUtils.showMessage(throwable.localizedMessage)
                   ownersDataChannel.send(PickOwnerUiState.Error(throwable))
               }
               .collect {
                   ownersDataChannel.send(PickOwnerUiState.Success(it))
               }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            firebaseAuthManager.signOutFlow()
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }
}