package com.example.fitnessfactory_client.data.beans

import com.example.fitnessfactory_client.utils.TimeUtils
import java.util.*

class PeriodObject(val startDate: Date = TimeUtils.getStartDate(Date()),
                   val endDate: Date = TimeUtils.getEndDate(Date())) {
}