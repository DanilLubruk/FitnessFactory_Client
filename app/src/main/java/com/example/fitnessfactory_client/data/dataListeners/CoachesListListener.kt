package com.example.fitnessfactory_client.data.dataListeners

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.Personnel
import com.example.fitnessfactory_client.utils.ResUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception

class CoachesListListener: BaseDataListener() {

    override fun getRoot(): String =
        FirestoreCollections.getOwnerCoachesCollection()

    fun startDataListener(): Flow<List<Personnel>> =
        callbackFlow {
            listenerRegistration = getCollection().addSnapshotListener { value, error ->
                if (error != null) {
                    throw error
                }
                if (value == null) {
                    throw Exception(ResUtils.getString(R.string.message_error_data_obtain))
                }

                val coachesList = value.toObjects(Personnel::class.java)
                trySend(coachesList)
            }

            awaitClose { listenerRegistration }
        }
}