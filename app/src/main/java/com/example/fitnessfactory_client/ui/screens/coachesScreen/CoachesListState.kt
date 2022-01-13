package com.example.fitnessfactory_client.ui.screens.coachesScreen

import com.example.fitnessfactory_client.data.models.AppUser

sealed class CoachesListState {
    data class Loaded(val coachesList: List<AppUser>): CoachesListState()
    object Loading: CoachesListState()
}