package com.example.fitnessfactory_client.ui.screens.homeScreen

sealed class GymsChainDataState {
    data class Loaded(var gymsChainDataState: GymsChainDataState): GymsChainDataState()
}