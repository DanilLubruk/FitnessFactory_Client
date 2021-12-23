package com.example.fitnessfactory_client.di

import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import dagger.Module
import dagger.Provides

@Module
class AppModule() {

    @AppScope
    @Provides
    fun provideFirebaseAuthManager(): FirebaseAuthManager {
        return FirebaseAuthManager()
    }
}