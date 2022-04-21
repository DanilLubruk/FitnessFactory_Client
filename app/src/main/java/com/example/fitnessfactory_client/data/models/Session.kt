package com.example.fitnessfactory_client.data.models

import com.example.fitnessfactory_client.utils.TimeUtils
import com.google.firebase.firestore.Exclude
import java.util.*

class Session() {

    companion object {
        const val ID_FIELD = "id"
        const val START_TIME_FIELD = "startTime"
        const val END_TIME_FIELD = "endTime"
        const val GYM_ID_FIELD = "gymId"
        const val SESSION_TYPE_ID_FIELD = "sessionTypeId"
        const val COACHES_EMAILS_FIELD = "coachesEmails"
        const val CLIENTS_EMAILS_FIELD = "clientsEmails"
    }

    lateinit var id: String

    var date: Date?
        @Exclude
        get() = startTime
        @Exclude
        set(value) {
            startTime = TimeUtils.setDatesDay(example = value, subject = startTime)
            endTime = TimeUtils.setDatesDay(example = value, subject = endTime)
        }

    val dateString: String
        @Exclude
        get() {
            return TimeUtils.dateToLocaleStr(date = startTime)
        }

    var startTime: Date? = null
        get
        set(value) {
            field = value
        }
    val startTimeString: String
        @Exclude
        get() {
            return TimeUtils.dateTo24HoursTime(date = startTime)
        }

    var endTime: Date? = null
        get
        set(value) {
            field = value
        }
    val endTimeString: String
        @Exclude
        get() {
            return TimeUtils.dateTo24HoursTime(date = endTime)
        }

    lateinit var gymId: String
    lateinit var sessionTypeId: String
    var coachesEmails: List<String>? = null
    var clientsEmails: List<String>? = null
}