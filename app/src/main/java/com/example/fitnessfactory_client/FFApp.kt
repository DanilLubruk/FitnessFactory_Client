package com.example.fitnessfactory_client

import android.app.Application
import com.example.fitnessfactory_client.di.AppComponent
import com.example.fitnessfactory_client.di.AppModule
import com.example.fitnessfactory_client.di.DaggerAppComponent

open class FFApp: Application() {

    lateinit var appComponent: AppComponent;

    companion object {
         lateinit var instance: FFApp
         private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initComponent()
    }

    protected fun initComponent() {
        appComponent =
            DaggerAppComponent
            .builder()
            .appModule(AppModule())
            .build()
    }
}