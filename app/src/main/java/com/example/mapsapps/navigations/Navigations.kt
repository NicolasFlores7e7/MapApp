package com.example.mapsapps.navigations

sealed class Routes(val route: String){
    object Login:Routes("login")
    object Map:Routes("map")
    object MarkerList:Routes("markerlist")
    object AddMarker:Routes("addmarker")
}