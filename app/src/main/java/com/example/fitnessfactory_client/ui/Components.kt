package com.example.fitnessfactory_client.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.ui.drawer.DrawerScreens
import com.example.fitnessfactory_client.utils.ResUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

object Components {

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun Drawer(
        modifier: Modifier = Modifier,
        onDestinationClicked: (route: String) -> Unit,
        logout: () -> Unit) {
        Column(modifier = modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp)) {
            Text(
                text = ResUtils.getString(R.string.app_name),
                style = MaterialTheme.typography.h3
            )

            DrawerScreens.getDrawerScreens().forEach { screen ->
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = screen.title,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.clickable {
                        onDestinationClicked(screen.navRoute)
                    }
                )
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start) {
                IconButton(onClick = { logout() } ) {
                    Icon(Icons.Sharp.Logout, contentDescription = "")
                }
            }
        }
    }

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun TopBar(title: String = "", buttonIcon: ImageVector, onButtonClicked: () -> Unit) {
        TopAppBar(
            title = {
                Text(
                    text = title
                )
            },
            navigationIcon = {
                IconButton(onClick = { onButtonClicked() } ) {
                    Icon(buttonIcon, contentDescription = "")
                }
            },
            backgroundColor = MaterialTheme.colors.primaryVariant
        )
    }
}