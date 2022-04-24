package com.example.fitnessfactory_client.ui.screens.authScreen

sealed class RegisterUiState {
    data class Success(var userId: String): RegisterUiState()
    data class Error(var exception: Throwable): RegisterUiState()
    class Loading: RegisterUiState()
}