package com.example.fitnessfactory_client.data.models

import com.example.fitnessfactory_client.utils.TimeUtils
import java.util.*

class Session() {

    companion object {
        const val ID_FIELD = "id"
        const val DATE_FIELD = "date"
        const val START_TIME_FIELD = "startTime"
        const val END_TIME_FIELD = "endTime"
        const val GYM_ID_FIELD = "gymId"
        const val SESSION_TYPE_ID_FIELD = "sessionTypeId"
        const val COACHES_IDS_FIELD = "coachesIds"
        const val CLIENTS_IDS_FIELD = "clientsIds"
    }

    lateinit var id: String
    var date: Date? = null
        get
        set(value) {
            field = value
            correctStartEndTimeDay()
        }

    var dateString: String = ""
        get() {
            return TimeUtils.dateToLocaleStr(date = date)
        }
        set

    var startTime: Date? = null
        get
        set(value) {
            field = value
            correctStartEndTimeDay()
        }

    var endTime: Date? = null
        get
        set(value) {
            field = value
            correctStartEndTimeDay()
        }

    lateinit var gymId: String
    lateinit var sessionTypeId: String
    var coachesIds: List<String>? = null
    var clientsIds: List<String>? = null

    private fun correctStartEndTimeDay() {
        if (!TimeUtils.isTheSameDay(date, startTime)) {
            startTime = TimeUtils.setDatesDay(date, startTime)
        }
        if (!TimeUtils.isTheSameDay(date, endTime)) {
            endTime = TimeUtils.setDatesDay(date, endTime)
        }
    }
}