package com.example.fitnessfactory_client.ui.activities.authActivity

sealed class RegisterUiState {
    data class Success(var usersEmail: String): RegisterUiState()
    data class Error(var exception: Throwable): RegisterUiState()
    class Loading: RegisterUiState()
}