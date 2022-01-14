package com.example.fitnessfactory_client.di

import com.example.fitnessfactory_client.data.dataListeners.*
import com.example.fitnessfactory_client.data.managers.*
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
        ownersRepository: OwnersRepository,
        ownerClientRepository: OwnerClientRepository
    ): AuthManager =
        AuthManager(
            firebaseAuthManager = firebaseAuthManager,
            usersRepository = usersRepository,
            clientsAccessRepository = clientsAccessRepository,
            ownersRepository = ownersRepository,
            ownerClientRepository = ownerClientRepository
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

    @AppScope
    @Provides
    fun provideCoachesListListener() =
        CoachesListListener()

    @AppScope
    @Provides
    fun provideSessionTypesDataListener() =
        SessionTypesListListener()

    @AppScope
    @Provides
    fun provideGymsListListener() =
        GymsListListener()

    @AppScope
    @Provides
    fun provideCoachesDataManager(
        ownerCoachRepository: OwnerCoachRepository,
        usersRepository: UsersRepository
    ) =
        CoachesDataManager(ownerCoachRepository = ownerCoachRepository, usersRepository = usersRepository)
}