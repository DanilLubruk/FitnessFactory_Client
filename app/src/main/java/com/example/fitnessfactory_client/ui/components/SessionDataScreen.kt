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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.window.Dialog
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.views.SessionView
import com.example.fitnessfactory_client.utils.ResUtils
import kotlinx.coroutines.launch

object SessionDataScreen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun SessionDataScreen(
        sessionData: SessionView,
        coachUsers: List<AppUser>,
        onItemAction: (String) -> Unit,
        itemActionName: String,
        modalBottomSheetState: ModalBottomSheetState
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
                val scope = rememberCoroutineScope()

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = colorResource(id = R.color.royalBlue)
                    ),
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.hide()
                            onItemAction(sessionData.session.id)
                        }
                    }) {
                    Text(text = itemActionName, style = MaterialTheme.typography.body1)
                }

                Spacer(modifier = Modifier.height(16.dp))

                DataScreenField.DataScreenField(
                    content = sessionData.session.dateString,
                    hint = stringResource(id = R.string.caption_date)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        DataScreenField.DataScreenField(
                            content = sessionData.session.startTimeString,
                            hint = stringResource(id = R.string.caption_start_time)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        DataScreenField.DataScreenField(
                            content = sessionData.session.endTimeString,
                            hint = stringResource(id = R.string.caption_end_time)
                        )
                    }
                }

                DataScreenField.DataScreenField(
                    content = sessionData.gymName,
                    hint = stringResource(id = R.string.caption_gym)
                )

                DataScreenField.DataScreenField(
                    content = sessionData.sessionTypeName,
                    hint = stringResource(id = R.string.caption_session_type)
                )

                if (coachUsers.isNotEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = ResUtils.getString(R.string.title_coaches_screen),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        itemsIndexed(coachUsers) { index, item ->
                            CoachListItemView.CoachListItemView(
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