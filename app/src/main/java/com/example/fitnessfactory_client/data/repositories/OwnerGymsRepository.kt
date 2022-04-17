package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.models.Personnel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OwnerGymsRepository : BaseRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getOwnerGymsCollection()

    fun getGymsFlow(): Flow<List<Gym>> =
        flow {
            emit(getGyms())
        }

    suspend fun getCoachGyms(coach: Personnel): List<Gym> =
        getQuerySnapshot(
            getCollection().whereIn(
                Gym.ID_FIELD,
                coach.gymsIds
            )
        ).toObjects(Gym::class.java)


    suspend fun getGyms(): List<Gym> =
        getQuerySnapshot(getCollection()).toObjects(Gym::class.java)
}