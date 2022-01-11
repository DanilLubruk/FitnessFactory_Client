package com.example.fitnessfactory_client.ui.components

import android.content.Context
import android.util.Log
import com.example.fitnessfactory_client.utils.CalendarDayUtils
import com.example.fitnessfactory_client.utils.TimeUtils
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.*

class HighlightedDaysCalendarView(
    context: Context,
    setCalendarPeriod: (Date, Date) -> Unit
) : MaterialCalendarView(context) {

    private var decorator: SessionDaysDecorator = SessionDaysDecorator()

    init {
        addDecorator(decorator)
        setOnMonthChangedListener { widget, date ->
            val currentDate = CalendarDayUtils.getDateFromCalendarDay(date)
            setCalendarPeriod(
                TimeUtils.getStartDate(currentDate),
                TimeUtils.getEndDate(currentDate)
            )
        }
    }

    fun updateSessions(sessions: List<Date>) {
        decorator.updateSessions(sessions = sessions)
        invalidateDecorators()
        Log.d("TAG", "sessions ${sessions.size}")
        invalidate()
    }
}