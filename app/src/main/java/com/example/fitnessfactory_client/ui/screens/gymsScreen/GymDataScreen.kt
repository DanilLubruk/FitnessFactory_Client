package com.example.fitnessfactory_client.ui.screens.gymsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.ui.components.DataScreenField
import com.example.fitnessfactory_client.utils.ResUtils

object GymDataScreen {

    @Composable
    fun GymDataScreen(
        gym: Gym,
        coaches: ArrayList<AppUser>,
        onDismissRequest: () -> Unit,
        showSessionsAction: (Gym) -> Unit,
    ) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
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
                        content = gym.name,
                        hint = stringResource(id = R.string.caption_name)
                    )

                    DataScreenField.DataScreenField(
                        content = gym.address,
                        hint = stringResource(id = R.string.caption_address)
                    )

                    if (coaches.isNotEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = ResUtils.getString(R.string.title_coaches_screen),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()

                        ) {
                            itemsIndexed(coaches) { index, item ->
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = item.name,
                                    textAlign = TextAlign.Left
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            backgroundColor = colorResource(id = R.color.royalBlue)
                        ),
                        onClick = {
                            showSessionsAction(gym)
                            onDismissRequest()
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
}