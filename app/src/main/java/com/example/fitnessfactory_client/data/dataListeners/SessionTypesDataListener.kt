package com.example.fitnessfactory_client.data.dataListeners

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.SessionType
import com.example.fitnessfactory_client.utils.ResUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception

class SessionTypesDataListener: BaseDataListener() {

    override fun getRoot(): String =
        FirestoreCollections.getSessionTypesCollection()

    fun startDataListener(): Flow<List<SessionType>> =
        callbackFlow {
            listenerRegistration = getCollection().addSnapshotListener { value, error ->
                if (error != null) {
                    throw error
                }
                if (value == null) {
                    throw Exception(ResUtils.getString(R.string.message_error_data_obtain))
                }

                val sessionTypes = value.toObjects(SessionType::class.java)
                trySend(sessionTypes)
            }

            awaitClose { listenerRegistration.remove() }
        }
}