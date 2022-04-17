package com.example.fitnessfactory_client.ui.screens.sessionTypesScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.models.SessionType
import com.example.fitnessfactory_client.ui.components.DataScreenField
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.StringUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SessionTypeDataScreen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun SessionTypeDataScreen(
        sessionType: SessionType,
        showSessionsAction: (SessionType) -> Unit,
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DataScreenField.DataScreenField(
                    content = sessionType.name,
                    hint = stringResource(id = R.string.caption_name)
                )

                DataScreenField.DataScreenField(
                    content = sessionType.peopleAmount.toString(),
                    hint = stringResource(id = R.string.caption_people_amount)
                )

                DataScreenField.DataScreenField(
                    content = sessionType.price.toString(),
                    hint = StringUtils.getCaptionPrice()
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = colorResource(id = R.color.royalBlue)
                    ),
                    onClick = {
                        showSessionsAction(sessionType)
                    }) {
                    Text(
                        text = stringResource(id = R.string.caption_show_sessions),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}