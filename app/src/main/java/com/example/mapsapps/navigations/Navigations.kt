package com.example.mapsapps.navigations

sealed class Routes(val route: String){
    object Login:Routes("login")
    object Map:Routes("map")
    object MarkerList:Routes("markerlist")
    object AddMarker:Routes("addmarker")
    object PhotoScreen:Routes("photo")
    object GaleryScreen:Routes("galery")
    object DetailScreen:Routes("detail")
    object Register:Routes("register")
}