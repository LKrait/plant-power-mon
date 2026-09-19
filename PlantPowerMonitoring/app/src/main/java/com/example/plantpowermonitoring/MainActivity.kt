package com.example.plantpowermonitoring

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Window

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(6, 23, 42)
        window.navigationBarColor = Color.rgb(6, 23, 42)
        setContentView(DashboardView(this))
    }
}
