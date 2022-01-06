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

    fun isLoggedInFlow(): Flow<Boolean> =
        flow {
            emit(isLoggedIn())
        }

    private fun isLoggedIn(): Boolean =
        mAuth.currentUser != null

    fun signOutFlow(): Flow<Boolean> =
        flow {
            emit(signOut())
        }

    private suspend fun signOut(): Boolean {
        mAuth.signOut()
        val isFirebaseSignedOut = mAuth.currentUser == null
        val isGoogleSignedOut = signOutGoogle()

        return isFirebaseSignedOut && isGoogleSignedOut
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