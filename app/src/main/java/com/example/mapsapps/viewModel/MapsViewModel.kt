package com.example.mapsapps.viewModel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapps.R
import com.example.mapsapps.data.UserPrefs
import com.example.mapsapps.firebase.Repository
import com.example.mapsapps.models.CustomMarker
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth

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
    val categoryToType = mapOf("Café" to 2131165283, "Bomberos" to 2131165309, "Hospital" to 2131165333, "Aeropuerto" to 2131165270, "Parque" to 2131165379)
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

    private val auth = FirebaseAuth.getInstance()
    private val _areWeLoggedIn = MutableLiveData(false)
    val areWeLoggedIn = _areWeLoggedIn

    private val _email = MutableLiveData<String>()
    val email = _email
    private val _password = MutableLiveData<String>()
    val password = _password
    private val _passwordCheck = MutableLiveData<String>()
    val passwordCheck = _passwordCheck
    private val _userId = MutableLiveData<String>()
    val userId = _userId
    private val _returnToLogIn = MutableLiveData(false)
    val returnToLogIn = _returnToLogIn
    private val _dialogOpener = MutableLiveData(false)
    val dialogOpener = _dialogOpener
    private val _editDialogOpener = MutableLiveData(false)
    val editDialogOpener = _editDialogOpener
    private val _areWeLoggedInAndRemembered = MutableLiveData(false)
    val areWeLoggedInAndRemembered = _areWeLoggedInAndRemembered
    fun addMarker(marker: CustomMarker) {
        val updatedMarkers = markers.value?.filter { it != marker }
        _markers.value?.apply { add(marker) }
        repository.addMarker(marker)
        markers.value = updatedMarkers as MutableList<CustomMarker>?
    }

    private val _saveData = MutableLiveData(false)
    val saveData = _saveData
    private val _loading = MutableLiveData(true)

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

//    fun editMarker(marker: CustomMarker, newMarker: CustomMarker) {
//        val updatedMarkers = markers.value?.map { if (it == newMarker) marker else it }
//        repository.addMarker(newMarker)
//        repository.removeMarker(marker)
//        markers.value = updatedMarkers as MutableList<CustomMarker>?
//    }
    fun deleteMarker(marker: CustomMarker) {
        val updatedMarkers = markers.value?.filter { it != marker }
        repository.removeMarker(marker)
        repository.deleteImage(marker.image)
        markers.value = updatedMarkers as MutableList<CustomMarker>?
    }

    fun setEditDialogOpener(value: Boolean) {
        _editDialogOpener.value = value
    }

    fun setMail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }
    fun setPasswordCheck(password: String) {
        _passwordCheck.value = password
    }
    fun setReturnToLogIn(value: Boolean) {
        _returnToLogIn.value = value
    }

    fun setOpenerDialog(value: Boolean) {
        _dialogOpener.value = value
    }


    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _returnToLogIn.value = true
                }else{
                    _returnToLogIn.value = false
                    Log.d("Error algo mas", "Error registering user: ${task.exception?.message}")
                }
            }
            .addOnFailureListener {
                _dialogOpener.value = true
                Log.d("Error algo mas", "Error registering user: ${it.message}")
            }
    }
    fun setSaveData(value: Boolean) {
        _saveData.value = value
    }

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userId.value = task.result?.user?.uid
                    _areWeLoggedIn.value = true
                } else {
                    _areWeLoggedIn.value = false
                    Log.d("Error", "Error logging in: ${task.exception?.message}")
                }
            }
            .addOnFailureListener{
                _dialogOpener.value = true
                Log.d("Error", "Error logging in: ${it.message}")
            }

    }

    fun logOut() {
        auth.signOut()
        _areWeLoggedIn.value = false
        _areWeLoggedInAndRemembered.value = false
    }

    fun handleCategorySelection(category: String) {
        if (category == "Todos") {
            getAllMarkers()
        } else {
            categoryToType[category]?.let { type ->
                getMarkersByType(type)
            }
        }
    }

    fun getMarkersByType(type: Int) {
        repository.getMarkersByType(type).observeForever { markers ->
            _markers.value = markers.toMutableList()
        }
    }

    fun getAllMarkers() {
        repository.getMarkersFromDataBase().addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("Firestore", "Listen failed.", error)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val markers =
                    snapshot.documents.map { repository.documentToMarker(it) }.toMutableList()
                _markers.value = markers
            } else {
                Log.d("Firestore", "Current data: null")
            }
        }
    }

}

