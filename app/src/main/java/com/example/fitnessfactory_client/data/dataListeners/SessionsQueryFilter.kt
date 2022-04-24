package com.example.fitnessfactory_client.data.dataListeners

import com.example.fitnessfactory_client.data.beans.CoachData
import com.example.fitnessfactory_client.data.beans.SessionsFilter
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.data.models.SessionType
import com.example.fitnessfactory_client.utils.TimeUtils
import com.google.firebase.firestore.Query
import java.util.*

object SessionsQueryFilter {

    fun getQuery(
        initialQuery: Query,
        startDate: Date,
        endDate: Date,
        sessionsFilter: SessionsFilter
    ): Query {
        var query = whereDateIs(query = initialQuery, startDate = startDate, endDate = endDate)

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
            query.whereArrayContains(Session.COACHES_IDS_FIELD, coachData.coachUser.id)
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
            .whereGreaterThanOrEqualTo(Session.START_TIME_FIELD, TimeUtils.getStartOfDayDate(startDate))
            .whereLessThan(Session.START_TIME_FIELD, TimeUtils.getStartOfNextDay(endDate))
}