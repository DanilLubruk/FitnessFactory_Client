package com.example.fitnessfactory_client.di

import com.example.fitnessfactory_client.ui.screens.appScreen.AppScreenViewModelFactory
import com.example.fitnessfactory_client.ui.screens.authScreen.AuthScreenViewModelFactory
import com.example.fitnessfactory_client.ui.screens.coachesScreen.CoachesScreenViewModelFactory
import com.example.fitnessfactory_client.ui.screens.gymsScreen.GymsScreenViewModelFactory
import com.example.fitnessfactory_client.ui.screens.homeScreen.HomeScreenViewModelFactory
import com.example.fitnessfactory_client.ui.screens.mySessionsScreen.MySessionsListScreenViewModelFactory
import com.example.fitnessfactory_client.ui.screens.sessionTypesScreen.SessionTypesScreenViewModelFactory
import com.example.fitnessfactory_client.ui.screens.splashScreen.SplashScreenViewModel
import com.example.fitnessfactory_client.ui.screens.splashScreen.SplashScreenViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@AppScope
@Singleton
interface AppComponent {

    fun inject(authScreenViewModelFactory: AuthScreenViewModelFactory)
    fun inject(splashScreenViewModelFactory: SplashScreenViewModelFactory)
    fun inject(appScreenViewModelFactory: AppScreenViewModelFactory)
    fun inject(mySessionsListScreenViewModelFactory: MySessionsListScreenViewModelFactory)
    fun inject(homeScreenViewModelFactory: HomeScreenViewModelFactory)
    fun inject(coachesScreenViewModelFactory: CoachesScreenViewModelFactory)
    fun inject(sessionTypesScreenViewModelFactory: SessionTypesScreenViewModelFactory)
    fun inject(gymsScreenViewModelFactory: GymsScreenViewModelFactory)
}