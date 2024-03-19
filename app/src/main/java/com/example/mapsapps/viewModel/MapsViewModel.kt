package com.example.mapsapps.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapps.R
import com.example.mapsapps.models.CustomMarker
import com.google.android.gms.maps.model.LatLng

class MapsViewModel : ViewModel() {

    private val _markers = MutableLiveData<MutableList<CustomMarker>>(mutableListOf())
    val markers = _markers
    private val _showBottomSheet = MutableLiveData(false)
    val showBottomSheet = _showBottomSheet
    private val _currentLatLng = MutableLiveData<LatLng>()
    val currentLatLng = _currentLatLng
    val iconsList = listOf(
        R.drawable.cafe,
        R.drawable.fireman,
        R.drawable.hospital,
        R.drawable.airport,
        R.drawable.park
    )
    private val _cameraPermission = MutableLiveData(false)
    val cameraPermission = _cameraPermission
    private val _shouldPermRationale = MutableLiveData(false)
    val shouldPermRationale = _shouldPermRationale
    private val _showPermDenied = MutableLiveData(false)
    val showPermDenied = _showPermDenied

    fun addMarker(marker: CustomMarker) {
        _markers.value?.apply { add(marker) }
    }

    fun setCameraPermission(granted: Boolean) {
        _cameraPermission.value = granted
    }
    fun setShouldPermRationale(should: Boolean) {
        _shouldPermRationale.value = should
    }

    fun setShowPermDenied(denied: Boolean) {
        _showPermDenied.value = denied
    }

}