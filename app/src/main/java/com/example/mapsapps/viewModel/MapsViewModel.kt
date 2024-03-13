package com.example.mapsapps.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapps.models.CustomMarker
import com.google.android.gms.maps.model.MarkerOptions

class MapsViewModel : ViewModel() {

    private val _markers = MutableLiveData<List<CustomMarker>>(emptyList())
    val markers = _markers
//    private val _showBottomSheet = MutableLiveData(false)
//    val showBottomSheet = _showBottomSheet

    fun addMarker(marker: CustomMarker) {
        val currentMarkers = _markers.value.orEmpty().toMutableList()
        currentMarkers.add(marker)
        _markers.value = currentMarkers.toList()
    }

//    fun showBottomSheetEnabler() {
//        if (showBottomSheet.value == true) {
//            _showBottomSheet.value = false
//        } else {
//            _showBottomSheet.value = true
//        }
//    }

}