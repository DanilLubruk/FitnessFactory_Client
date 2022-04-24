package com.example.fitnessfactory_client.ui.screens.coachesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.ui.components.DataScreenField
import com.example.fitnessfactory_client.ui.components.GymListItemView
import com.example.fitnessfactory_client.utils.ResUtils

object CoachDataScreen {

    @Composable
    fun CoachDataScreen(
        coach: AppUser,
        gymsList: List<Gym>,
        showSessionsAction: (String) -> Unit,
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
        ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            backgroundColor = colorResource(id = R.color.royalBlue)
                        ),
                        onClick = {
                            showSessionsAction(coach.id)
                        }) {
                        Text(
                            text = stringResource(id = R.string.caption_show_sessions),
                            style = MaterialTheme.typography.body1
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    DataScreenField.DataScreenField(
                        content = coach.name,
                        hint = stringResource(id = R.string.caption_name)
                    )

                    DataScreenField.DataScreenField(
                        content = coach.email,
                        hint = stringResource(id = R.string.caption_email)
                    )

                    if (gymsList.isNotEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = ResUtils.getString(R.string.caption_gyms),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body1
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            itemsIndexed(gymsList) { index, item ->
                                GymListItemView.GymListItemView(
                                    item = item,
                                    onTap = { }
                                )
                            }
                        }


                    }
                }
        }
    }
}
