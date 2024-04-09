package com.example.mapsapps.models

import com.google.android.gms.maps.model.LatLng

data class CustomMarker(
    val name: String,
    val description: String,
    val position : LatLng,
    val icon: Int,
    val image: String,
    val owner:String,
)
