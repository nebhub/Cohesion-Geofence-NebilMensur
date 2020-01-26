package com.example.geofence

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class CohesionGeofenceManager {

    companion object {
        val LOCATION_PERMISSION_REQUEST = 10000
    }

    private lateinit var geofencingClient: GeofencingClient
    private lateinit var context: Context
    var canRequestPermission = false

    private lateinit var request: GeofencingRequest
    private val dataApi : DataApi = DataApi()

    fun initialize(context: Context) {
        this.context = context
        if (!hasPermission(context) && canRequestPermission) {
            requestPermission(context)
            return
        }
        setupGeofences()
    }

    fun setupGeofences() {
        geofencingClient = LocationServices.getGeofencingClient(context)
        dataApi.fetchGeofences(context, object : DataApi.GeofenceFetchCallback {
            override fun onSuccess(geofences: List<CohesionGeofence>?) {
                if (geofences == null) {
                    return
                }
                handleGeofences(geofences.map { it.toGeofence() })
            }

            override fun Onfailure(msg: String) {
                Log.e("CohesionGeofence", "cannot fetch geofences")
            }
        })
    }

    private fun handleGeofences(geofences: List<Geofence>) {
        request = getGeofenceRequest(geofences)
        geofencingClient.addGeofences(request, CohesionGoefenceReceiver.getPendingIntent(context))
    }

    private fun getGeofenceRequest(geofences: List<Geofence>): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER)
            .addGeofences(geofences).build()
    }

    private fun hasPermission(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
                == PackageManager.PERMISSION_GRANTED)

    }

    private fun requestPermission(context: Context): Boolean {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity,  arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION

            ), LOCATION_PERMISSION_REQUEST)
        } else {
            return true
        }
        return false
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: kotlin.Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            setupGeofences()
        }
    }

}

