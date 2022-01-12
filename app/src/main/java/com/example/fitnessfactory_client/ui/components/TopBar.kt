package com.example.fitnessfactory_client.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.fitnessfactory_client.R
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

object TopBar {

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun TopBar(
        title: String = "",
        buttonIcon: ImageVector,
        onButtonClicked: () -> Unit,
        actionName: String = "",
        action: () -> Unit = {}
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = { onButtonClicked() }) {
                    Icon(buttonIcon, contentDescription = "", tint = Color.White)
                }
            },
            backgroundColor = colorResource(id = R.color.royalBlue),
            actions = {
                Text(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { action() },
                    text = actionName,
                    color = Color.White
                )
            }
        )
    }
}