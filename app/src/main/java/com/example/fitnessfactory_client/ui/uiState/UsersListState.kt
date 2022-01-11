package com.example.fitnessfactory_client.ui.uiState

import com.example.fitnessfactory_client.data.models.AppUser

sealed class UsersListState {
    data class Loaded(val usersList: List<AppUser>): UsersListState()
    data class Error(val throwable: Throwable): UsersListState()
}
