package com.example.projecttravel

import android.app.Application
import com.example.projecttravel.data.AppContainer
import com.example.projecttravel.data.DefaultAppContainer

class TravelApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
