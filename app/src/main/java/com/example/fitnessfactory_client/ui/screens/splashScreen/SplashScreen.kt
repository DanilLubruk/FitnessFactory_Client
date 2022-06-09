package com.example.fitnessfactory_client.ui.screens.splashScreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.ui.components.ListLoadingView
import com.example.fitnessfactory_client.utils.ResUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
        openHomeScreen: () -> Unit
    ) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.isStatusBarVisible = false
        }
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

        LaunchedEffect(key1 = Unit) {
            viewModel.checkLoggedIn()
        }

        Scaffold {
            Surface {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = ResUtils.getString(R.string.app_name),
                        style = MaterialTheme.typography.h5)
                    Spacer(modifier = Modifier.height(16.dp))
                    Icon(
                        modifier = Modifier
                            .height(80.dp)
                            .width(80.dp),
                        imageVector = Icons.Filled.FitnessCenter,
                        contentDescription = "",
                        tint = colorResource(id = R.color.gold),
                    )
                }
            }
        }


    }
}