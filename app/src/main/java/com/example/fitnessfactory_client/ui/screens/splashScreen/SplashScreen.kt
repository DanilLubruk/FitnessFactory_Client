package com.example.fitnessfactory_client.ui.screens.splashScreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

object SplashScreen {

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun SplashScreen(
        lifecycle: Lifecycle,
        openAuthScreen: () -> Unit,
        openHomeScreen: () -> Unit) {
        val viewModel: SplashScreenViewModel = viewModel(factory = SplashScreenViewModelFactory())

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAuthState.collect { isAuthState ->
                    when (isAuthState) {
                        is IsAuthState.LoggedIn -> openHomeScreen()
                        is IsAuthState.LoggedOut -> openAuthScreen()
                        is IsAuthState.Error -> openAuthScreen()
                    }
                }
            }
        }

        viewModel.checkLoggedIn()
    }
}