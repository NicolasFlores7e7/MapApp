package com.example.mapsapps.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapps.R
import com.example.mapsapps.firebase.Repository
import com.example.mapsapps.models.CustomMarker
import com.google.android.gms.maps.model.LatLng
import java.io.ByteArrayOutputStream

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

    private val _photoTaken = MutableLiveData<Bitmap?>()
    val photoTaken = _photoTaken
    private val repository = Repository()
    private val _imageList = MutableLiveData<MutableList<String>>(mutableListOf())
    val imageList = _imageList
    fun addMarker(marker: CustomMarker) {
        _markers.value?.apply { add(marker) }
        repository.addMarker(marker)
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

    fun addPhotoToRepo(bitmap: Bitmap, imageName: String) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        repository.uploadImage(data, imageName)
        val image = repository.getImageUrl(imageName).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    val imageUrl = document.getString("imageData")
                    if (imageUrl != null) {
                        _imageList.value?.apply { add(imageUrl) }
                        _imageList.value?.forEach { url ->
                            println("URL de la imagen: $url")
                        }
                    } else {
                        println("El campo 'imageData' no existe en el documento")
                    }
                } else {
                    println("No se pudo obtener el documento")
                }
            } else {
                println("Error al obtener el documento: ${task.exception?.message}")
            }
        }
    }
}

