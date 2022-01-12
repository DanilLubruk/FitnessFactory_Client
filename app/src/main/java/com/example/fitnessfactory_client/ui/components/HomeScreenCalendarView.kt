package com.example.fitnessfactory_client.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.utils.ConvertUtils
import io.github.boguszpawlowski.composecalendar.day.DayState
import java.time.LocalDate
import java.util.*

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.utils.TimeUtils
import io.github.boguszpawlowski.composecalendar.Calendar
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.selection.SelectionState
import java.time.YearMonth


object HomeScreenCalendarView {

    @Composable
    fun HomeScreenCalendarView(
        sessionsList: List<Session>,
        setListenerDate: (Date) -> Unit,
        setCalendarListenerDates: (Date, Date) -> Unit
    ) {
        val calendarState = rememberDaySelectionState(setListenerDate = setListenerDate)

        Column(modifier =
        Modifier
            .verticalScroll(rememberScrollState())) {
            Calendar(
                dayContent = { dayState -> MyDay(sessionsList = sessionsList, state = dayState) },
                calendarState = calendarState,
            )
        }

        var calendarMonth by rememberSaveable { mutableStateOf(YearMonth.now().minusMonths(1)) }
        val setCurrentMonth: (YearMonth) -> Unit = {
            calendarMonth = it
        }

        OnMonthSelected(
            currentMonth = calendarMonth,
            newMonth = calendarState.monthState.currentMonth,
            setCurrentMonth = setCurrentMonth,
            setCalendarListenerDates = setCalendarListenerDates
        )
    }

    @Composable
    fun OnMonthSelected(
        currentMonth: YearMonth,
        newMonth: YearMonth,
        setCurrentMonth: (YearMonth) -> Unit,
        setCalendarListenerDates: (Date, Date) -> Unit
    ) {
        if (currentMonth != newMonth) {
            setCalendarListenerDates(
                TimeUtils.getStartDate(newMonth),
                TimeUtils.getEndDate(newMonth)
            )
            setCurrentMonth(newMonth)
        }
    }

    @Composable
    fun MyDay(
        sessionsList: List<Session>,
        state: DayState<DaySelectionState>,
        modifier: Modifier = Modifier,
        currentDayColor: Color = colorResource(id = R.color.royalBlue),
        onClick: (LocalDate) -> Unit = {},
    ) {
        val date = state.date
        val selectionState = state.selectionState
        val isDayOccupied: Boolean = isDayOccupied(sessionsList = sessionsList, dayState = state)

        val isSelected = selectionState.isDateSelected(date)
        val borderColor: BorderStroke?
        if (state.isCurrentDay) {
            borderColor = BorderStroke(1.dp, currentDayColor)
        } else if (isSelected) {
            borderColor = BorderStroke(1.dp, colorResource(id = R.color.gold))
        } else {
            borderColor = null
        }

        Card(
            modifier = modifier
                .aspectRatio(1f)
                .padding(2.dp),
            elevation = if (state.isFromCurrentMonth) 4.dp else 0.dp,
            border = borderColor,
            contentColor = if (isDayOccupied) Color.Black else Color.LightGray
        ) {
            Box(
                modifier = Modifier.clickable {
                    onClick(date)
                    selectionState.onDateSelected(date)
                },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = date.dayOfMonth.toString())
            }
        }
    }

    private fun isDayOccupied(
        sessionsList: List<Session>,
        dayState: DayState<DaySelectionState>
    ): Boolean {
        sessionsList.forEach { session ->
            session.date?.let {
                val sessionDate: LocalDate = ConvertUtils.dateToLocalDate(it)
                if (dayState.date.isEqual(sessionDate)) {
                    return true
                }
            }
        }

        return false
    }

    class DaySelectionState(
        initialSelection: LocalDate? = null,
        val setListenerDate: (Date) -> Unit = {}
    ) : SelectionState {

        private var daySelection by mutableStateOf(initialSelection)

        override fun isDateSelected(date: LocalDate): Boolean =
            date == daySelection

        override fun onDateSelected(date: LocalDate) {
            daySelection = date
            setListenerDate(ConvertUtils.localDateToDate(date))
        }

        companion object {
            @Suppress("FunctionName") // Factory function
            fun Saver(): Saver<DaySelectionState, Any> = Saver(
                save = { it.daySelection?.toString() },
                restore = { restored ->
                    val selection = (restored as? String)?.let { LocalDate.parse(it) }
                    DaySelectionState(selection)
                }
            )
        }
    }

    @Composable
    private fun rememberDaySelectionState(
        setListenerDate: (Date) -> Unit = {},
        initialMonth: YearMonth = YearMonth.now(),
        initialSelection: LocalDate? = LocalDate.now(),
        monthState: MonthState = rememberSaveable(saver = MonthState.Saver()) {
            MonthState(initialMonth = initialMonth)
        },
        selectionState: DaySelectionState = rememberSaveable(saver = DaySelectionState.Saver()) {
            DaySelectionState(
                initialSelection = initialSelection,
                setListenerDate = setListenerDate
            )
        }
    ): CalendarState<DaySelectionState> = remember { CalendarState(monthState, selectionState) }
}