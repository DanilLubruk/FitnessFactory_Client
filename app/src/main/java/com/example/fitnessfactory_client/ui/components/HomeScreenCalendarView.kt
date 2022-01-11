package com.example.fitnessfactory_client.ui.components

import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.utils.CalendarDayUtils
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.*

object HomeScreenCalendarView {

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