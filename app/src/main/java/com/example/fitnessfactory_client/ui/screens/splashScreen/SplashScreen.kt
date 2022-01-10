package com.example.fitnessfactory_client.ui.screens.splashScreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.utils.ResUtils
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
        openHomeScreen: () -> Unit
    ) {
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
                    Text(text = ResUtils.getString(R.string.caption_greetings))
                    Spacer(modifier = Modifier.height(5.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(16.dp)
                            .width(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }


    }
}