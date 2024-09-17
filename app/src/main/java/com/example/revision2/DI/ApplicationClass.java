package com.example.revision2.DI;



import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass extends Application()
        {
        override fun onCreate() {
        super.onCreate()
        // Initialize any global components or libraries if needed
        }
        }
