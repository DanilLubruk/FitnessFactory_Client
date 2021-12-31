package com.example.fitnessfactory_client.ui.activities.authActivity

sealed class RegisterUiState {
    class Success: RegisterUiState()
    data class Error(var exception: Throwable): RegisterUiState()
    class Loading: RegisterUiState()
}