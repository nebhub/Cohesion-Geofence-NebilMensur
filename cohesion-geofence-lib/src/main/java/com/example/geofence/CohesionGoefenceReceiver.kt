package com.example.geofence

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

open class CohesionGoefenceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent)
        if (event.hasError()) {
            Log.e(TAG, GeofenceStatusCodes.getStatusCodeString(event.errorCode))
            return
        }

        val geofences = event.triggeringGeofences
        geofences.forEach { DataApi().sendReport(context, it) }

    }

    companion object {
        fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, CohesionGoefenceReceiver::class.java)
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val TAG: String = CohesionGoefenceReceiver::class.java.simpleName
    }
}
