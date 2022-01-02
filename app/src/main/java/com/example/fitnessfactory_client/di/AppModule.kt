package com.example.fitnessfactory_client.di

import com.example.fitnessfactory_client.data.managers.AuthManager
import com.example.fitnessfactory_client.data.repositories.OwnersRepository
import com.example.fitnessfactory_client.data.repositories.UsersAccessRepository
import com.example.fitnessfactory_client.data.repositories.UsersRepository
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @AppScope
    @Provides
    fun provideFirebaseAuthManager(): FirebaseAuthManager =
        FirebaseAuthManager()

    @AppScope
    @Provides
    fun provideUsersRepository(): UsersRepository =
        UsersRepository()

    @AppScope
    @Provides
    fun provideUsersAccessRepository(): UsersAccessRepository =
        UsersAccessRepository()

    @AppScope
    @Provides
    fun provideOwnersRepository(): OwnersRepository =
        OwnersRepository()

    @AppScope
    @Provides
    fun provideAuthManager(
        usersAccessRepository: UsersAccessRepository,
        ownersRepository: OwnersRepository
    ): AuthManager =
        AuthManager(
            usersAccessRepository = usersAccessRepository,
            ownersRepository = ownersRepository
        )
}