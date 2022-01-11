package com.example.fitnessfactory_client.ui.uiState

class ListStateOperator {

    var listState: ListState = ListState.Loading
    private set

    fun listLoaded() {
        listState = ListState.Loaded
    }

    fun listLoading() {
        listState = ListState.Loading
    }

    fun listEmpty() {
        listState = ListState.Empty
    }

    fun isLoading(): Boolean =
        listState is ListState.Loading
}