package com.example.fitnessfactory_client

import android.app.Application
import com.example.fitnessfactory_client.di.AppComponent
import com.example.fitnessfactory_client.di.AppModule
import com.example.fitnessfactory_client.di.DaggerAppComponent

class FFApp: Application() {

    lateinit var appComponent: AppComponent;

    companion object {
         lateinit var instance: FFApp
    }

    override fun onCreate() {
        super.onCreate()
        setInstance(this)
        initComponent()
    }

    private fun setInstance(instance: FFApp) {
        FFApp.instance = instance;
    }

    protected fun initComponent() {
        appComponent =
            DaggerAppComponent
            .builder()
            .appModule(AppModule())
            .build()
    }
}