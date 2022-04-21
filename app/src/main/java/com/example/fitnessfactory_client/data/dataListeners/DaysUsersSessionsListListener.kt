package com.example.fitnessfactory_client.data.dataListeners

import android.util.Log
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.beans.SessionsFilter
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.TimeUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import java.util.*

class DaysUsersSessionsListListener : BaseDataListener() {

    override fun getRoot(): String =
        FirestoreCollections.getSessionsCollection()

    fun startDataListener(
        sessionsFilter: SessionsFilter,
        startDate: Date,
        endDate: Date,
        usersEmail: String
    ): Flow<List<Session>> =
        callbackFlow {
            listenerRegistration =
                SessionsQueryFilter.getQuery(
                    initialQuery = getCollection(),
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

                        val sessions = value.toObjects(Session::class.java).filter {
                            it.clientsEmails?.contains(usersEmail) ?: false
                        }
                        trySend(sessions)
                    }

            awaitClose { listenerRegistration.remove() }
        }
}