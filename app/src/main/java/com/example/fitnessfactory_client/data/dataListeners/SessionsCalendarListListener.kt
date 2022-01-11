package com.example.fitnessfactory_client.data.dataListeners

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.TimeUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import java.util.*

class SessionsCalendarListListener: BaseDataListener() {

    override fun getRoot(): String =
        FirestoreCollections.getSessionsCollection()

    fun startDataListener(startDate: Date, endDate: Date): Flow<List<Session>> =
        callbackFlow {
            listenerRegistration =
                getCollection()
                    .whereGreaterThan(Session.DATE_FIELD, TimeUtils.getStartOfDayDate(startDate))
                    .whereLessThan(Session.DATE_FIELD, TimeUtils.getEndOfDayDate(endDate))
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            throw error
                        }
                        if (value == null) {
                            throw Exception(ResUtils.getString(R.string.message_error_data_obtain))
                        }

                        val sessions: List<Session> = value.toObjects(Session::class.java)
                        trySend(sessions)
                    }

            awaitClose { listenerRegistration }
        }
}