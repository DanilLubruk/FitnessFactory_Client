package com.example.fitnessfactory_client.data.beans

import android.os.Bundle
import com.example.fitnessfactory_client.data.models.Owner

class OwnersData {

    companion object {
        const val INVITED_OWNERS_LIST_EXTRA = "INVITED_OWNERS_LIST_EXTRA"
        const val INVITED_OWNERS_SIZE_EXTRA = "INVITED_OWNERS_SIZE_EXTRA"
        const val ALL_OWNERS_LIST_EXTRA = "ALL_OWNERS_LIST_EXTRA"
        const val ALL_OWNERS_SIZE_EXTRA = "ALL_OWNERS_SIZE_EXTRA"
        const val BUNDLE_LOADED = "BUNDLE_LOADED"
    }

    var invitedOwnersList: ArrayList<Owner> = ArrayList()
    var allOwnersList: ArrayList<Owner> = ArrayList()

    fun saveState(savedState: Bundle) {
        if (invitedOwnersList.size != 0) {
            val invitedOwnersBundle = Bundle()
            invitedOwnersList.forEachIndexed { index, owner ->
                owner.saveState(savedState = invitedOwnersBundle, index = index + 1)
            }
            savedState.putBundle(INVITED_OWNERS_LIST_EXTRA, invitedOwnersBundle)
            savedState.putInt(INVITED_OWNERS_SIZE_EXTRA, invitedOwnersList.size)
        }

        if (allOwnersList.size != 0) {
            val allOwnersBundle = Bundle()
            allOwnersList.forEachIndexed { index, owner ->
                owner.saveState(savedState = allOwnersBundle, index = index + 1)
            }
            savedState.putBundle(ALL_OWNERS_LIST_EXTRA, allOwnersBundle)
            savedState.putInt(ALL_OWNERS_SIZE_EXTRA, allOwnersList.size)
        }
    }

    fun restoreState(savedState: Bundle) {
        val invitedOwnersSize = savedState.getInt(INVITED_OWNERS_SIZE_EXTRA)
        savedState.getBundle(INVITED_OWNERS_LIST_EXTRA)?.let {
            val invitedOwnersBundle = it
            for (i in 1..invitedOwnersSize) {
                val owner = Owner()
                owner.restoreState(savedState = invitedOwnersBundle, index = i)
                invitedOwnersList.add(owner)
            }
        }

        val allOwnersSize = savedState.getInt(ALL_OWNERS_SIZE_EXTRA)
        savedState.getBundle(ALL_OWNERS_LIST_EXTRA)?.let {
            val allOwnersBundle =  it
            for (i in 1..allOwnersSize) {
                val owner = Owner()
                owner.restoreState(savedState = allOwnersBundle, index = i)
                allOwnersList.add(owner)
            }
        }
    }
}