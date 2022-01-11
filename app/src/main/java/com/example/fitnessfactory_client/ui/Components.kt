package com.example.fitnessfactory_client.ui

import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.views.SessionView
import com.example.fitnessfactory_client.utils.CalendarDayUtils
import com.example.fitnessfactory_client.ui.drawer.DrawerScreens
import com.example.fitnessfactory_client.utils.DialogUtils
import com.example.fitnessfactory_client.utils.ResUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

object Components {

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun Drawer(
        modifier: Modifier = Modifier,
        onDestinationClicked: (route: String) -> Unit,
        logout: () -> Unit
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 24.dp, top = 48.dp)
        ) {
            Text(
                text = ResUtils.getString(R.string.app_name),
                style = MaterialTheme.typography.h4
            )

            DrawerScreens.getDrawerScreens().forEach { screen ->
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = screen.title,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onDestinationClicked(screen.navRoute)
                        }
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                IconButton(onClick = { logout() }) {
                    Icon(Icons.Sharp.Logout, contentDescription = "")
                }
            }
        }
    }

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun TopBar(title: String = "", buttonIcon: ImageVector, onButtonClicked: () -> Unit) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = { onButtonClicked() }) {
                    Icon(buttonIcon, contentDescription = "", tint = Color.White)
                }
            },
            backgroundColor = colorResource(id = R.color.royalBlue)
        )
    }

    @Composable
    fun HomeScreenCalendarView(
        setListenerDate: (Date) -> Unit
    ) {
        AndroidView(
            modifier = Modifier.wrapContentSize(),
            factory = { context ->
                MaterialCalendarView(
                    ContextThemeWrapper(
                        context,
                        R.style.CalenderViewCustom
                    )
                )
            },
            update = { view ->
                view.setOnDateChangedListener { widget, date, selected ->
                    setListenerDate(CalendarDayUtils.getDateFromCalendarDay(date))
                }
            }
        )
    }


}