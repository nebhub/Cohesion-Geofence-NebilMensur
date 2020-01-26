package com.example.geofence

import android.content.Context

import android.util.Log

import com.google.android.gms.location.Geofence
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader

import java.io.InputStreamReader


class DataApi {
    var result: List<CohesionGeofence> = emptyList()

     fun fetchGeofences(context: Context, callback: GeofenceFetchCallback) {
         loadGeofences(context)
         callback.onSuccess(result)
     }

    private fun loadGeofences(context: Context): List<CohesionGeofence> {
        val inputStream = context.resources.openRawResource(R.raw.geofences)
        val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        val gson = Gson()
        val jsonReader = gson.newJsonReader(reader)

        result = gson.fromJson(jsonReader, object : TypeToken<List<CohesionGeofence>>() {}.type)
        return result

    }

    fun sendReport(context: Context, geofence: Geofence) {
        val list = loadGeofences(context)
        list.forEach { if (it.id == geofence.requestId) {
            //todo send data to some server
            Log.i("CohesionGeofence", "Sending com.example.geofence data: " + it.name +
                    "\n" + "latitude: " + it.latitude + "\n" + "longitude: "+ it.longitute)
        } }

    }
    interface GeofenceFetchCallback {
        fun onSuccess(geofences: List<CohesionGeofence>?)
        fun Onfailure(msg: String)
    }
}