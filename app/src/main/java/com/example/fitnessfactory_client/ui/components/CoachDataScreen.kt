package com.example.fitnessfactory_client.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.example.fitnessfactory_client.utils.ResUtils

object CoachDataScreen {

    @Composable
    fun CoachDataScreen(
        coach: AppUser,
        gymsList: List<Gym>,
        onDismissRequest: () -> Unit,
        showSessionsAction: (String) -> Unit,
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        OutlinedTextField(
                            value = coach.name,
                            onValueChange = {},
                            label = { Text(stringResource(id = R.string.caption_name)) },
                            textStyle = MaterialTheme.typography.body1,
                            colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .alpha(0f)
                                .clickable(onClick = {})
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        OutlinedTextField(
                            value = coach.email,
                            onValueChange = {},
                            label = { Text(stringResource(id = R.string.caption_email)) },
                            textStyle = MaterialTheme.typography.body1,
                            colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .alpha(0f)
                                .clickable(onClick = {})
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (gymsList.isNotEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = ResUtils.getString(R.string.caption_gyms),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()

                        ) {
                            itemsIndexed(gymsList) { index, item ->
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
                            showSessionsAction(coach.email)
                            onDismissRequest()
                        }) {
                        Text(text = stringResource(id = R.string.caption_show_sessions), style = MaterialTheme.typography.body1)
                    }
                }
            }
        }
    }
}