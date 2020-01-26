package com.example.cohesion_geofence_nebilmensur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.geofence.CohesionGeofenceManager

class MainActivity : AppCompatActivity() {

    lateinit var geofenceManager: CohesionGeofenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        geofenceManager =  CohesionGeofenceManager()
        geofenceManager.canRequestPermission = true
        geofenceManager.initialize(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        geofenceManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
