package com.example.mapsapps.models

import com.google.android.gms.maps.model.LatLng

data class CustomMarker(
    val name: String,
    val position : LatLng,

)
