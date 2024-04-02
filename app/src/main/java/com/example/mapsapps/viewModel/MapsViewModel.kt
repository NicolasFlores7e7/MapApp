package com.example.mapsapps.viewModel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapps.R
import com.example.mapsapps.firebase.Repository
import com.example.mapsapps.models.CustomMarker
import com.google.android.gms.maps.model.LatLng
import java.io.ByteArrayOutputStream

class MapsViewModel : ViewModel() {

    private val repository = Repository()
    private val _markerName = MutableLiveData<String>()
    val markerName = _markerName
    private val _markerDescription = MutableLiveData<String>()
    val markerDescription = _markerDescription
    private val _iconNum = MutableLiveData<Int>()
    val iconNum = _iconNum
    private val _selectedMarker = MutableLiveData<CustomMarker>()
    val selectedMarker = _selectedMarker
    private val _markers = MutableLiveData<MutableList<CustomMarker>>().apply {
        repository.getMarkersFromDataBase().addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("Firestore", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val markers =
                    snapshot.documents.map { repository.documentToMarker(it) }.toMutableList()
                this.value = markers
            } else {
                Log.d("Firestore", "Current data: null")
            }
        }
    }
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

    private val _photoTaken = MutableLiveData<Bitmap?>()
    val photoTaken = _photoTaken


    fun addMarker(marker: CustomMarker) {
        val updatedMarkers = markers.value?.filter { it != marker }
        _markers.value?.apply { add(marker) }
        repository.addMarker(marker)
        markers.value = updatedMarkers as MutableList<CustomMarker>?
    }
    fun setMarkerName(name: String) {
        _markerName.value = name
    }
    fun setMarkerDescription(description: String) {
        _markerDescription.value = description
    }
    fun setSelectedMarker(marker: CustomMarker) {
        _selectedMarker.value = marker
    }
    fun setIconNum(num: Int) {
        _iconNum.value = num
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

    fun addPhotoTaken(bitmap: Bitmap) {
        _photoTaken.value = bitmap
    }

    fun addPhotoToRepo(imageUri: Uri): LiveData<String> {
        return repository.uploadImage(imageUri)
    }

    fun deleteMarker(marker: CustomMarker) {
        val updatedMarkers = markers.value?.filter { it != marker }
        repository.removeMarker(marker)
        repository.deleteImage(marker.image)
        markers.value = updatedMarkers as MutableList<CustomMarker>?
    }
}

