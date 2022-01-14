package com.example.fitnessfactory_client.data.dataListeners

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.utils.ResUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception

class GymsListListener : BaseDataListener() {

    override fun getRoot(): String =
        FirestoreCollections.getOwnerGymsCollection()

    fun startDataListener(): Flow<List<Gym>> =
        callbackFlow {
            listenerRegistration =
                getCollection()
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            throw error
                        }
                        if (value == null) {
                            throw Exception(ResUtils.getString(R.string.message_error_data_obtain))
                        }

                        val gymsList = value.toObjects(Gym::class.java)
                        trySend(gymsList)
                    }

            awaitClose { listenerRegistration.remove() }
        }
}