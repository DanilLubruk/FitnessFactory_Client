package com.example.fitnessfactory_client.data.models

import com.example.fitnessfactory_client.data.FirestoreCollections

class Owner() {

    companion object {
        const val ID_FIELD = "id"
        fun getOrganisationNamePath() =
            FirestoreCollections.organisationDataCollectionValue +
                    "/" +
                    OrganisationData.NAME_FIELD
    }

    lateinit var id: String
    lateinit var organisationName: String

    fun getOrganisationNamePath() =
        id +
                "/" +
                Owner.getOrganisationNamePath()
}