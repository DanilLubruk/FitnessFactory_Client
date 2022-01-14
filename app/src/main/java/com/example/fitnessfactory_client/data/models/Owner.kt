package com.example.fitnessfactory_client.data.models

import android.os.Bundle
import com.example.fitnessfactory_client.data.FirestoreCollections

class Owner() {

    companion object {
        const val ID_FIELD = "id"
        fun getOrganisationNamePath() =
            FirestoreCollections.organisationDataCollectionValue +
                    "/" +
                    OrganisationData.NAME_FIELD

        const val ID_FIELD_EXTRA = "ID_FIELD"
        const val ORG_NAME_EXTRA = "ORG_NAME"
    }

    lateinit var id: String
    lateinit var organisationName: String

    fun getOrganisationNamePath() =
        id +
                "/" +
                Owner.getOrganisationNamePath()

    fun saveState(savedState: Bundle, index: Int) {
        savedState.putString(ID_FIELD_EXTRA + index, id)
        savedState.putString(ORG_NAME_EXTRA + index, organisationName)
    }

    fun restoreState(savedState: Bundle, index: Int) {
        savedState.getString(ID_FIELD_EXTRA + index)?.let {
            id = it
        }
        savedState.getString(ORG_NAME_EXTRA + index)?.let {
            organisationName = it
        }
    }
}