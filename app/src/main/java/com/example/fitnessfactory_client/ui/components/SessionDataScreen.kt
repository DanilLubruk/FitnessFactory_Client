package com.example.fitnessfactory_client.ui.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.views.SessionView
import com.example.fitnessfactory_client.utils.ResUtils

object SessionDataScreen {

    @Composable
    fun SessionDataScreen(
        sessionData: SessionView,
        coachUsers: List<AppUser>,
        onDismissRequest: () -> Unit,
        onItemAction: (String) -> Unit,
        itemActionName: String
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
                            value = sessionData.session.dateString,
                            onValueChange = {},
                            label = { Text(ResUtils.getString(R.string.caption_date)) },
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                OutlinedTextField(
                                    value = sessionData.session.startTimeString,
                                    onValueChange = {},
                                    label = { Text(ResUtils.getString(R.string.caption_start_time)) },
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
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                OutlinedTextField(
                                    value = sessionData.session.endTimeString,
                                    onValueChange = {},
                                    label = { Text(ResUtils.getString(R.string.caption_end_time)) },
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
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        OutlinedTextField(
                            value = sessionData.gymName,
                            onValueChange = {},
                            label = { Text(ResUtils.getString(R.string.caption_gym)) },
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
                            value = sessionData.sessionTypeName,
                            onValueChange = {},
                            label = { Text(ResUtils.getString(R.string.caption_session_type)) },
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

                    if (coachUsers.isNotEmpty()) {
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
                            itemsIndexed(coachUsers) { index, item ->
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
                            onItemAction(sessionData.session.id)
                            onDismissRequest()
                        }) {
                        Text(text = itemActionName, style = MaterialTheme.typography.body1)
                    }
                }
            }
        }
    }
}