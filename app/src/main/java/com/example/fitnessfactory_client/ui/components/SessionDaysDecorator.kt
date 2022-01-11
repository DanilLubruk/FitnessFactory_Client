package com.example.fitnessfactory_client.ui.components

import android.graphics.drawable.Drawable
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.utils.CalendarDayUtils
import com.example.fitnessfactory_client.utils.ResUtils
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*
import kotlin.collections.ArrayList

class SessionDaysDecorator(): DayViewDecorator {

    private var sessions: List<Date> = ArrayList()
    private val drawable: Drawable = ResUtils.getDrawable(R.drawable.calendar_day_selector)!!

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.let { calendarDay ->
            sessions.forEach { sessionsDate ->
                if (sessionsDate == CalendarDayUtils.getDateFromCalendarDay(calendarDay)) {
                    return true
                }
            }
        }

        return false
    }

    override fun decorate(view: DayViewFacade) =
        view.setBackgroundDrawable(drawable)

    fun updateSessions(sessions: List<Date>) {
        this.sessions = sessions
    }
}