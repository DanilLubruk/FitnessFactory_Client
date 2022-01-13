package com.example.fitnessfactory_client.data.dataListeners

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.beans.CoachData
import com.example.fitnessfactory_client.data.beans.SessionsFilter
import com.example.fitnessfactory_client.data.models.*
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.TimeUtils
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class SessionsCalendarListListener : BaseDataListener() {

    override fun getRoot(): String =
        FirestoreCollections.getSessionsCollection()

    fun startDataListener(
        startDate: Date,
        endDate: Date,
        sessionsFilter: SessionsFilter
    ): Flow<ArrayList<Session>> =
        callbackFlow {
            listenerRegistration =
                getQuery(
                    startDate = startDate,
                    endDate = endDate,
                    sessionsFilter = sessionsFilter
                )
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            throw error
                        }
                        if (value == null) {
                            throw Exception(ResUtils.getString(R.string.message_error_data_obtain))
                        }

                        val sessions: ArrayList<Session> =
                            value.toObjects(Session::class.java) as ArrayList<Session>
                        trySend(sessions)
                    }

            awaitClose { listenerRegistration }
        }

    private fun getQuery(
        startDate: Date,
        endDate: Date,
        sessionsFilter: SessionsFilter
    ): Query {
        var query = whereDateIs(query = getCollection(), startDate = startDate, endDate = endDate)

        if (!sessionsFilter.doFilter) {
            return query
        }

        query = whereSessionHasCoach(query = query, coachData = sessionsFilter.coachData)
        query = whereSessionTypeIs(query = query, sessionType = sessionsFilter.sessionType)
        query = whereSessionsGymIs(query = query, gym = sessionsFilter.gym)

        return query
    }

    private fun whereSessionHasCoach(query: Query, coachData: CoachData): Query =
        if (coachData.doFilter)
            query.whereArrayContains(Session.COACHES_IDS_FIELD, coachData.id)
        else
            query

    private fun whereSessionTypeIs(query: Query, sessionType: SessionType): Query =
        if (sessionType.doFilter)
            query.whereEqualTo(Session.SESSION_TYPE_ID_FIELD, sessionType.id)
        else
            query


    private fun whereSessionsGymIs(query: Query, gym: Gym): Query =
        if (gym.doFilter)
            query.whereEqualTo(Session.GYM_ID_FIELD, gym.id)
        else
            query

    private fun whereDateIs(
        query: Query,
        startDate: Date,
        endDate: Date
    ): Query =
        query
            .whereGreaterThan(Session.DATE_FIELD, TimeUtils.getStartOfDayDate(startDate))
            .whereLessThan(Session.DATE_FIELD, TimeUtils.getEndOfDayDate(endDate))
}