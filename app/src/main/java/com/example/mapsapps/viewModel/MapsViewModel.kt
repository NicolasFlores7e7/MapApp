package com.example.mapsapps.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.MarkerOptions

class MapsViewModel : ViewModel() {

    private val _markers = MutableLiveData<MutableList<MarkerOptions>>()
    val markers = _markers


    fun addMarker(marker: MarkerOptions) {
        _markers.value?.apply { add(marker) }
    }
}