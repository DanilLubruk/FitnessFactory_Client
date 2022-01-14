package com.example.fitnessfactory_client.ui.screens.gymsScreen

import com.example.fitnessfactory_client.data.models.Gym

sealed class GymsListState {
    data class Loaded(val gymsList: ArrayList<Gym>): GymsListState()
    object Loading: GymsListState()
}