package com.example.fitnessfactory_client.data.beans

import android.os.Bundle
import androidx.compose.runtime.saveable.Saver
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.ui.components.HomeScreenCalendarView
import com.example.fitnessfactory_client.utils.TimeUtils
import java.time.LocalDate
import java.util.*
import kotlin.reflect.KProperty

class PeriodObject(
    val startDate: Date = TimeUtils.getStartDate(Date()),
    val endDate: Date = TimeUtils.getEndDate(Date())
) {
}