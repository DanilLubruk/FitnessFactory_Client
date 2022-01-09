package com.example.fitnessfactory_client.utils

import androidx.core.os.LocaleListCompat
import com.example.fitnessfactory_client.FFApp
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    const val MEDIUM_DATE_FORMAT = "d MMM YYYY"

    fun getStartOfDayDate(date: Date?): Date {
        if (date != null) {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val firstHourOfDay = calendar.getActualMinimum(Calendar.HOUR_OF_DAY)
            val firstMinute = calendar.getActualMinimum(Calendar.MINUTE)
            val firstSecond = calendar.getActualMinimum(Calendar.SECOND)
            calendar[Calendar.HOUR_OF_DAY] = firstHourOfDay
            calendar[Calendar.MINUTE] = firstMinute
            calendar[Calendar.SECOND] = firstSecond
            return calendar.time
        }
        return Date()
    }

    fun getEndOfDayDate(date: Date?): Date {
        if (date != null) {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val firstHourOfDay = calendar.getActualMaximum(Calendar.HOUR_OF_DAY)
            val firstMinute = calendar.getActualMaximum(Calendar.MINUTE)
            val firstSecond = calendar.getActualMaximum(Calendar.SECOND)
            calendar[Calendar.HOUR_OF_DAY] = firstHourOfDay
            calendar[Calendar.MINUTE] = firstMinute
            calendar[Calendar.SECOND] = firstSecond
            return calendar.time
        }
        return Date()
    }

    fun setDatesDay(example: Date?, subject: Date?): Date {
        val exampleCalendar = Calendar.getInstance()
        exampleCalendar.time = example
        val subjectCalendar = Calendar.getInstance()
        subjectCalendar.time = subject
        subjectCalendar[Calendar.YEAR] = exampleCalendar[Calendar.YEAR]
        subjectCalendar[Calendar.MONTH] = exampleCalendar[Calendar.MONTH]
        subjectCalendar[Calendar.DAY_OF_MONTH] = exampleCalendar[Calendar.DAY_OF_MONTH]
        return subjectCalendar.time
    }

    fun isTheSameDay(comparableDate: Date?, comparedDate: Date?): Boolean {
        var comparableDate = comparableDate
        var comparedDate = comparedDate
        if (comparableDate == null) {
            comparableDate = Date()
        }
        if (comparedDate == null) {
            comparedDate = Date()
        }
        val comparable = Calendar.getInstance()
        comparable.time = comparableDate
        val compared = Calendar.getInstance()
        compared.time = comparedDate
        return isTheSameDay(comparable, compared)
    }

    fun isTheSameDay(comparable: Calendar, compared: Calendar): Boolean {
        return comparable[Calendar.YEAR] == compared[Calendar.YEAR] && comparable[Calendar.MONTH] == comparable[Calendar.MONTH] && comparable[Calendar.DAY_OF_MONTH] == comparable[Calendar.DAY_OF_MONTH]
    }

    fun dateToLocaleStr(date: Date?): String {
        if (date == null) {
            return ""
        }

        try {
            val list = LocaleListCompat.getDefault()
            val current = list[0]
            return DateFormat.getDateInstance(DateFormat.MEDIUM, current).format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val dateFormat = android.text.format.DateFormat.getDateFormat(
            FFApp.instance
        )
        return dateFormat.format(date)
    }

    fun dateTo24HoursTime(date: Date?): String {
        return if (date == null)
            ""
        else
            get24HoursDateFormat().format(date)
    }

    private fun get24HoursDateFormat(): SimpleDateFormat {
        return SimpleDateFormat("HH:mm", Locale.UK)
    }
}