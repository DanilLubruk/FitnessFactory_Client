package com.example.fitnessfactory_client.ui.screens.homeScreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.ui.Components
import com.example.fitnessfactory_client.utils.ResUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

object HomeScreen {

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun HomeScreen(openDrawer: () -> Unit, logout: () -> Unit) {
        Column(modifier = Modifier.fillMaxSize()) {
            Components.TopBar(
                title = ResUtils.getString(R.string.title_home_screen),
                buttonIcon = Icons.Filled.Menu,
                onButtonClicked = { openDrawer() })

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "homeScreen")
                Button(onClick = { logout() }) {
                    Text(text = "logOut")
                }
            }
        }
    }
}