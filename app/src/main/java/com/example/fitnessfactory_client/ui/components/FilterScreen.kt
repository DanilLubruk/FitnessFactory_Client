package com.example.fitnessfactory_client.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.GymsChainData
import com.example.fitnessfactory_client.data.beans.SessionsFilter
import java.util.*

object FilterScreen {

    private const val NO_FILTER = 0
    private const val NO_ELEMENT_FOUND = -1

    @Composable
    fun FilterScreen(
        onDismissRequest: () -> Unit,
        sessionsFilter: SessionsFilter,
        chainData: GymsChainData,
        setFilter: (SessionsFilter) -> Unit
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
                    var selectedGymIndex = NO_FILTER
                    chainData.gyms.forEachIndexed { index, gym ->
                        if (gym.id == sessionsFilter.gym.id) {
                            selectedGymIndex = index
                        }
                    }
                    var selectedGym by remember { mutableStateOf(chainData.gyms[selectedGymIndex]) }
                    DropDownItem(
                        items = chainData.gyms,
                        selectedItem = selectedGym,
                        setSelectedItem = { gym -> selectedGym = gym },
                        hint = stringResource(id = R.string.caption_gym)
                    )

                    var selectedTypeIndex = NO_FILTER
                    chainData.sessionTypes.forEachIndexed { index, type ->
                        if (type.id == sessionsFilter.sessionType.id) {
                            selectedTypeIndex = index
                        }
                    }
                    var selectedSessionType by remember { mutableStateOf(chainData.sessionTypes[selectedTypeIndex]) }
                    DropDownItem(
                        items = chainData.sessionTypes,
                        selectedItem = selectedSessionType,
                        setSelectedItem = { sessionType -> selectedSessionType = sessionType },
                        hint = stringResource(id = R.string.caption_session_type)
                    )

                    var selectedCoachIndex = NO_FILTER
                    chainData.coaches.forEachIndexed { index, coach ->
                        if (coach.id == sessionsFilter.coachData.id) {
                            selectedCoachIndex = index
                        }
                    }
                    var selectedCoach by remember { mutableStateOf(chainData.coaches[selectedCoachIndex]) }
                    DropDownItem(
                        items = chainData.coaches,
                        selectedItem = selectedCoach,
                        setSelectedItem = { coach -> selectedCoach = coach },
                        hint = stringResource(id = R.string.title_coaches_screen)
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
                            setFilter(
                                SessionsFilter(
                                    gym = selectedGym,
                                    sessionType = selectedSessionType,
                                    coachData = selectedCoach
                                )
                            )
                            onDismissRequest()
                        }) {
                        Text(
                            text = stringResource(id = R.string.caption_filter),
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun <T> DropDownItem(
        items: List<T>,
        selectedItem: T,
        setSelectedItem: (T) -> Unit,
        hint: String
    ) {
        var toogle by remember { mutableStateOf(false) }
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                OutlinedTextField(
                    value = selectedItem.toString(),
                    onValueChange = {},
                    label = { Text(hint) },
                    textStyle = MaterialTheme.typography.body1,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.White
                    )
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(0f)
                        .clickable(onClick = { toogle = true })
                )
            }

            DropDownList.DropDownList(
                isToogled = toogle,
                items = items,
                selectItem = { item ->
                    setSelectedItem(item)
                },
                setToogle = { toogled -> toogle = toogled }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}