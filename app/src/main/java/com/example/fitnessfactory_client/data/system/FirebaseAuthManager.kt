package com.example.fitnessfactory_client.data.system

import com.example.fitnessfactory_client.FFApp
import com.example.fitnessfactory_client.data.ObfuscateData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager(private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()) {

    fun signOutFlow(): Flow<Unit> =
        flow {
            signOut()
            emit(Unit)
        }

    private suspend fun signOut() {
        mAuth.signOut()
        signOutGoogle()
    }

    private suspend fun signOutGoogle(): Boolean {
        getGoogleSignInClient().signOut().await()

        return true
    }

    private fun getGoogleSignInClient(): GoogleSignInClient =
        GoogleSignIn.getClient(FFApp.instance, getSignInOptions())

    private fun getSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ObfuscateData.getWebClientId())
            .requestEmail()
            .build()
    }
}