package com.example.fitnessfactory_client.ui.uiState

sealed class ListState {
    object Loaded: ListState()
    object Loading: ListState()
    object Empty: ListState()
}
