package com.example.fitnessfactory_client.di

import com.example.fitnessfactory_client.data.dataListeners.DaysSessionsListListener
import com.example.fitnessfactory_client.data.dataListeners.DaysUsersSessionsListListener
import com.example.fitnessfactory_client.data.dataListeners.SessionsCalendarListListener
import com.example.fitnessfactory_client.data.managers.AuthManager
import com.example.fitnessfactory_client.data.managers.CoachesAccessManager
import com.example.fitnessfactory_client.data.managers.GymsChainDataManager
import com.example.fitnessfactory_client.data.managers.SessionsDataManager
import com.example.fitnessfactory_client.data.repositories.*
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
    fun provideUsersAccessRepository(): ClientsAccessRepository =
        ClientsAccessRepository()

    @AppScope
    @Provides
    fun provideOwnersRepository(): OwnersRepository =
        OwnersRepository()

    @AppScope
    @Provides
    fun provideAuthManager(
        firebaseAuthManager: FirebaseAuthManager,
        usersRepository: UsersRepository,
        clientsAccessRepository: ClientsAccessRepository,
        ownersRepository: OwnersRepository
    ): AuthManager =
        AuthManager(
            firebaseAuthManager = firebaseAuthManager,
            usersRepository = usersRepository,
            clientsAccessRepository = clientsAccessRepository,
            ownersRepository = ownersRepository
        )

    @AppScope
    @Provides
    fun provideSessionViewRepository() =
        SessionViewRepository()

    @AppScope
    @Provides
    fun provideDaysSessionsListListener() =
        DaysSessionsListListener()

    @AppScope
    @Provides
    fun provideDaysUsersSessionsListListener() =
        DaysUsersSessionsListListener()

    @AppScope
    @Provides
    fun provideSessionsDataManager(
        firebaseAuthManager: FirebaseAuthManager,
        ownerClientRepository: OwnerClientRepository,
        sessionsRepository: SessionsRepository,
        clientSessionsRepository: ClientSessionsRepository
    ) =
        SessionsDataManager(
            firebaseAuthManager = firebaseAuthManager,
            ownerClientRepository = ownerClientRepository,
            sessionsRepository = sessionsRepository,
            clientSessionsRepository = clientSessionsRepository
        )

    @AppScope
    @Provides
    fun provideOwnerClientRepository() =
        OwnerClientRepository()

    @AppScope
    @Provides
    fun provideSessionsRepository() =
        SessionsRepository()

    @AppScope
    @Provides
    fun provideClientSessionsRepository() =
        ClientSessionsRepository()

    @AppScope
    @Provides
    fun provideSessionsCalendarListListener() =
        SessionsCalendarListListener()

    @AppScope
    @Provides
    fun provideCoachesAccessManager(
        ownerCoachRepository: OwnerCoachRepository,
        usersRepository: UsersRepository
    ) =
        CoachesAccessManager(
            ownerCoachRepository = ownerCoachRepository,
            usersRepository = usersRepository
        )

    @AppScope
    @Provides
    fun provideOwnerCoachRepository() =
        OwnerCoachRepository()

    @AppScope
    @Provides
    fun provideGymsChainData(
        ownerGymsRepository: OwnerGymsRepository,
        sessionTypesRepository: SessionTypesRepository,
        ownerCoachRepository: OwnerCoachRepository,
        usersRepository: UsersRepository
    ) =
        GymsChainDataManager(
            ownerGymsRepository = ownerGymsRepository,
            sessionTypesRepository = sessionTypesRepository,
            ownerCoachRepository = ownerCoachRepository,
            usersRepository = usersRepository
        )

    @AppScope
    @Provides
    fun provideOwnerGymsRepository() =
        OwnerGymsRepository()

    @AppScope
    @Provides
    fun provideSessionTypesRepository() =
        SessionTypesRepository()
}