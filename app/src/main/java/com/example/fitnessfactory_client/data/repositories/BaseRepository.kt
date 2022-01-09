package com.example.fitnessfactory_client.data.repositories

import com.example.fitnessfactory_client.data.FirestoreCollections
import com.example.fitnessfactory_client.data.firestoreCollections.CollectionOperator
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

open class BaseRepository: CollectionOperator() {

    override fun getRoot(): String =
        FirestoreCollections.getBaseCollection()

    protected suspend fun getQuerySnapshot(query: Query): QuerySnapshot =
        query.get().await()

    protected suspend fun getEntitiesAmount(query: Query): Int
        = query.get().await().documents.size

    protected fun isEntityUnique(documents: List<DocumentSnapshot>) =
        documents.size == 1
}