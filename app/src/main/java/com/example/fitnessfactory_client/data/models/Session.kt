package com.example.fitnessfactory_client.data.models

import com.example.fitnessfactory_client.utils.TimeUtils
import com.google.firebase.firestore.Exclude
import java.util.*

class Session() {

    companion object {
        const val ID_FIELD = "id"
        const val DATE_FIELD = "date"
        const val START_TIME_FIELD = "startTime"
        const val END_TIME_FIELD = "endTime"
        const val GYM_ID_FIELD = "gymId"
        const val SESSION_TYPE_ID_FIELD = "sessionTypeId"
        const val COACHES_EMAILS_FIELD = "coachesEmails"
        const val CLIENTS_EMAILS_FIELD = "clientsEmails"
    }

    lateinit var id: String

    var dateValue: Date?
        get() = Date(date)
        set(value) {
            if (value != null) {
                date = value.time
            }
            correctStartEndTimeDay()
        }

    var date: Long = 0
        get
        set(value) {
            field = value
            correctStartEndTimeDay()
        }

    var dateString: String = ""
        get() {
            return TimeUtils.dateToLocaleStr(date = dateValue)
        }
        set

    var startTime: Date? = null
        get
        set(value) {
            field = value
            correctStartEndTimeDay()
        }
    var startTimeString: String = ""
        get() {
            return TimeUtils.dateTo24HoursTime(date = startTime)
        }
        set

    var endTime: Date? = null
        get
        set(value) {
            field = value
            correctStartEndTimeDay()
        }
    var endTimeString: String = ""
        get() {
            return TimeUtils.dateTo24HoursTime(date = endTime)
        }
        set

    lateinit var gymId: String
    lateinit var sessionTypeId: String
    var coachesEmails: List<String>? = null
    var clientsEmails: List<String>? = null

    private fun correctStartEndTimeDay() {
        if (!TimeUtils.isTheSameDay(dateValue, startTime)) {
            startTime = TimeUtils.setDatesDay(dateValue, startTime)
        }
        if (!TimeUtils.isTheSameDay(dateValue, endTime)) {
            endTime = TimeUtils.setDatesDay(dateValue, endTime)
        }
    }
}