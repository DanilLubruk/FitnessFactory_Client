package com.example.fitnessfactory_client.utils

import java.time.LocalDate
import java.time.YearMonth
import java.util.*

object ConvertUtils {

    fun dateToLocalDate(date: Date): LocalDate {
        val calendar = Calendar.getInstance()
        calendar.time = date

        return LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun localDateToDate(localDate: LocalDate): Date {
        val calendar = Calendar.getInstance()
        calendar.set(localDate.year, localDate.month.value - 1, localDate.dayOfMonth)

        return calendar.time
    }

    fun dateFromYearMonth(month: YearMonth): Date {
        val calendar = Calendar.getInstance()
        calendar.set(month.year, month.month.value, 0)

        return calendar.time
    }
}