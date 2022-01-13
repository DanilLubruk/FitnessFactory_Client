package com.example.fitnessfactory_client.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.TopBarAction

object TopBar {

    @Composable
    fun TopBar(
        title: String = "",
        buttonIcon: ImageVector,
        onButtonClicked: () -> Unit,
        actions: List<TopBarAction> = ArrayList()
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
                actions.forEach {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clickable { it.action() },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(end = 8.dp),
                            text = it.actionName,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        )
    }
}