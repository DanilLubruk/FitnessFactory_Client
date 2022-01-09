package com.example.fitnessfactory_client.ui.screens.mySessionsScreen

sealed class ListState {
    object Loaded: ListState()
    object Loading: ListState()
    object Empty: ListState()
}
