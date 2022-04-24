package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.models.Personnel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class OwnerGymsRepository : BaseRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getOwnerGymsCollection()

    fun getGymsFlow(): Flow<List<Gym>> =
        flow {
            emit(getGyms())
        }

    suspend fun getCoachGyms(coach: Personnel): List<Gym>  {
        val gyms = ArrayList<Gym>()
        getCollection().get().await().toObjects(Gym::class.java).forEach {
            if (coach.gymsIds?.contains(it.id) == true) {
                gyms.add(it)
            }
        }

        return gyms
    }


    suspend fun getGyms(): List<Gym> =
        getQuerySnapshot(getCollection()).toObjects(Gym::class.java)
}