package com.example.geofence

import com.google.android.gms.location.Geofence
import com.google.gson.annotations.SerializedName


class CohesionGeofence(@SerializedName("requestID") var id: String,
                       @SerializedName("latitude") var latitude: Double,
                       @SerializedName("longitude") var longitute: Double) {
    @SerializedName("name")
    var name: String = ""
    @SerializedName("transition")
    var transition: String = TRANSITION_ENTER
    @SerializedName("radius")
    var radius: Float = RADIUS_DEFAULT
    @SerializedName("expirationDuration")
    var expirationDuration: Long = EXPIRATION_DURATION_DEFAULT
    @SerializedName("delay")
    var delay: Int = DELAY_DEFAULT

    fun toGeofence(): Geofence {
        return Geofence.Builder().setRequestId(id)
            .setCircularRegion(latitude, longitute, radius)
            .setTransitionTypes(getGeofenceDuration(transition))
            .setLoiteringDelay(delay)
            .setExpirationDuration(expirationDuration).build()
    }

    companion object {
        val TRANSITION_ENTER: String = "enter"
        val TRANSITION_EXIT: String = "exit"
        val TRANSITION_ENTER_OR_EXIT: String = "enter_or_exit"
        val TRANSTIOSN_DWELLING: String = "dwelling"
        val RADIUS_DEFAULT: Float = 100f
        val EXPIRATION_DURATION_DEFAULT: Long = 36000000
        val DELAY_DEFAULT: Int = 0

        fun getGeofenceDuration(string: String): Int {
            return when (string) {
                TRANSITION_ENTER -> Geofence.GEOFENCE_TRANSITION_ENTER
                TRANSITION_EXIT -> Geofence.GEOFENCE_TRANSITION_EXIT
                TRANSITION_ENTER_OR_EXIT -> Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT
                TRANSTIOSN_DWELLING -> Geofence.GEOFENCE_TRANSITION_DWELL
                else -> Geofence.GEOFENCE_TRANSITION_DWELL

            }
        }
    }
}
