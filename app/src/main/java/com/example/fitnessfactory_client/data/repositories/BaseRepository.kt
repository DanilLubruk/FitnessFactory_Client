package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.firestoreCollections.BaseCollection
import com.example.fitnessfactory_client.data.firestoreCollections.CollectionOperator
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

open class BaseRepository: CollectionOperator() {

    override fun getRoot(): String =
        BaseCollection.getBaseCollection()

    protected suspend fun getEntitiesAmount(query: Query): Int
        = query.get().await().documents.size

}