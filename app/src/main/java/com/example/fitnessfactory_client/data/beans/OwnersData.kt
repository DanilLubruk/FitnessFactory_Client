package com.example.fitnessfactory_client.data.beans

import android.os.Bundle
import com.example.fitnessfactory_client.data.models.Owner

class OwnersData {

    companion object {
        const val INVITED_OWNERS_LIST_EXTRA = "INVITED_OWNERS_LIST_EXTRA"
        const val INVITED_OWNERS_SIZE_EXTRA = "INVITED_OWNERS_SIZE_EXTRA"
        const val ALL_OWNERS_LIST_EXTRA = "ALL_OWNERS_LIST_EXTRA"
        const val ALL_OWNERS_SIZE_EXTRA = "ALL_OWNERS_SIZE_EXTRA"
    }

    lateinit var invitedOwnersList: ArrayList<Owner>
    lateinit var allOwnersList: ArrayList<Owner>

    fun saveState(savedState: Bundle) {
        val invitedOwnersBundle = Bundle()
        invitedOwnersList.forEachIndexed { index, owner ->
            owner.saveState(savedState = invitedOwnersBundle, index = index)
        }
        savedState.putBundle(INVITED_OWNERS_LIST_EXTRA, invitedOwnersBundle)
        savedState.putInt(INVITED_OWNERS_SIZE_EXTRA, invitedOwnersList.size)

        val allOwnersBundle = Bundle()
        allOwnersList.forEachIndexed { index, owner ->
            owner.saveState(savedState = allOwnersBundle, index = index)
        }
        savedState.putBundle(ALL_OWNERS_LIST_EXTRA, invitedOwnersBundle)
        savedState.putInt(ALL_OWNERS_SIZE_EXTRA, allOwnersList.size)
    }

    fun restoreState(savedState: Bundle) {
        val invitedOwnersSize = savedState.getInt(INVITED_OWNERS_SIZE_EXTRA)
        val invitedOwnersBundle = savedState.getBundle(INVITED_OWNERS_LIST_EXTRA) as Bundle
        for (i in 0..invitedOwnersSize) {
            val owner = Owner()
            owner.restoreState(savedState = invitedOwnersBundle, index = i)
            invitedOwnersList.add(owner)
        }

        val allOwnersSize = savedState.getInt(ALL_OWNERS_SIZE_EXTRA)
        val allOwnersBundle = savedState.getBundle(ALL_OWNERS_LIST_EXTRA) as Bundle
        for (i in 0..allOwnersSize) {
            val owner = Owner()
            owner.restoreState(savedState = allOwnersBundle, index = i)
            allOwnersList.add(owner)
        }
    }
}