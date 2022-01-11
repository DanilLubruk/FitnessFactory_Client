package com.example.fitnessfactory_client.utils

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

object CalendarDayUtils {

    fun isDayTheDate(date: Date, calendarDay: CalendarDay): Boolean {
        var calendarDate = getDateFromCalendarDay(calendarDay = calendarDay)
        calendarDate = TimeUtils.getStartOfDayDate(date = calendarDate)

        return TimeUtils.getStartOfDayDate(date = date) == calendarDate
    }

    fun getDateFromCalendarDay(calendarDay: CalendarDay): Date {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, calendarDay.year)
        calendar.set(Calendar.MONTH, (calendarDay.month - 1))
        calendar.set(Calendar.DAY_OF_MONTH, calendarDay.day)

        return TimeUtils.getStartOfDayDate(calendar.time)
    }
}