package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.models.Gym

class OwnerGymsRepository: BaseRepository() {

    override fun getRoot(): String =
        FirestoreCollections.getOwnerGymsCollection()

    suspend fun getGyms(): List<Gym> =
        getQuerySnapshot(getCollection()).toObjects(Gym::class.java)
}