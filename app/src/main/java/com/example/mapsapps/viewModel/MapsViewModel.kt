package com.example.mapsapps.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapps.models.CustomMarker
import com.google.android.gms.maps.model.MarkerOptions

class MapsViewModel : ViewModel() {

    private val _markers = MutableLiveData(
        mutableListOf<CustomMarker>())
    val markers = _markers
    val _showBottomSheet = MutableLiveData(false)
    val showBottomSheet = _showBottomSheet

    fun addMarker(marker: CustomMarker) {
        val list = _markers.value
        if (list != null) {
            list.add(marker)
        }
        _markers.value = list
        println("marcadores viewmodel "+ (_markers.value?.size ?:0 ))
    }

    fun showBottomSheetEnabler() {
        if (showBottomSheet.value == true) {
            _showBottomSheet.value = false
        } else {
            _showBottomSheet.value = true
        }
    }

}