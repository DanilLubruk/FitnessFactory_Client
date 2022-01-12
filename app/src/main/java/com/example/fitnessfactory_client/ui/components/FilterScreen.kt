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
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.GymsChainData
import com.example.fitnessfactory_client.data.beans.SessionsFilter
import com.example.fitnessfactory_client.utils.ResUtils
import java.util.*

object FilterScreen {

    @Composable
    fun FilterScreen(
        onDismissRequest: () -> Unit,
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

                    Text(
                        text = stringResource(id = R.string.title_set_filters).uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.h5
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    var selectedGym by remember { mutableStateOf(chainData.gyms[0]) }
                    DropDownItem(
                        items = chainData.gyms,
                        selectedItem = selectedGym,
                        setSelectedItem = { gym -> selectedGym = gym })

                    var selectedSessionType by remember { mutableStateOf(chainData.sessionTypes[0]) }
                    DropDownItem(
                        items = chainData.sessionTypes,
                        selectedItem = selectedSessionType,
                        setSelectedItem = { sessionType -> selectedSessionType = sessionType })

                    var selectedCoach by remember { mutableStateOf(chainData.coaches[0]) }
                    DropDownItem(
                        items = chainData.coaches,
                        selectedItem = selectedCoach,
                        setSelectedItem = { coach -> selectedCoach = coach })

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
                                    selectedGym,
                                    selectedSessionType,
                                    selectedCoach
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
    fun <T> DropDownItem(items: List<T>, selectedItem: T, setSelectedItem: (T) -> Unit) {
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
                    label = { Text(ResUtils.getString(R.string.caption_gym)) },
                    textStyle = MaterialTheme.typography.body1,
                    colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
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