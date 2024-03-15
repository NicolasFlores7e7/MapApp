package com.example.mapsapps.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapps.models.CustomMarker
import com.google.android.gms.maps.model.LatLng

class MapsViewModel : ViewModel() {

    private val _markers = MutableLiveData<MutableList<CustomMarker>>(mutableListOf())
    val markers = _markers
    private val _showBottomSheet = MutableLiveData(false)
    val showBottomSheet = _showBottomSheet
    private val _currentLatLng = MutableLiveData<LatLng>()
    val currentLatLng = _currentLatLng

    fun addMarker(marker: CustomMarker) {
        _markers.value?.apply { add(marker) }
    }



}