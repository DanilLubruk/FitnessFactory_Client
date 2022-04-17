package com.example.fitnessfactory_client.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessfactory_client.data.models.Gym
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

object GymListItemView {

    @Composable
    fun GymListItemView(item: Gym, onTap: (Gym) -> Unit) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .pointerInput(item) {
                detectTapGestures(
                    onPress = { },
                    onDoubleTap = { },
                    onLongPress = {},
                    onTap = { onTap(item) }
                )
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = item.name,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    color = Color.Gray,
                    text = item.address,
                    fontSize = 14.sp
                )
            }
        }
    }
}