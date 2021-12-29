package com.example.fitnessfactory_client.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthActivityViewModel
@Inject constructor(private val firebaseAuthManager: FirebaseAuthManager,
                    private val usersRepository: UsersRepository) : ViewModel() {

    private val mutableRegisterUiState: MutableStateFlow<RegisterUiState> = MutableStateFlow(RegisterUiState.Success())

    val registerUiState: StateFlow<RegisterUiState> = mutableRegisterUiState

    fun registerUser(usersName: String, usersEmail: String) {
        viewModelScope.launch {
            usersRepository.registerUserOperation(usersName = usersName, usersEmail = usersEmail)
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    mutableRegisterUiState.value = RegisterUiState.Error(throwable)
                }
                .collect {
                    mutableRegisterUiState.value = RegisterUiState.Success()
                }
        }
    }
}