package com.example.fitnessfactory_client.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.fitnessfactory_client.R

object ListLoadingView {

    @Composable
    fun ListLoadingView() {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp),
                strokeWidth = 2.dp,
                color = colorResource(id = R.color.royalBlue)
            )
        }
    }
}