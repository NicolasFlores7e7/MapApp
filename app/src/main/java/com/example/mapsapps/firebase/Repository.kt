package com.example.mapsapps.firebase


import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mapsapps.models.CustomMarker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Repository {
    private val database = FirebaseFirestore.getInstance()


    fun addMarker(marker: CustomMarker) {
        database.collection("markers")
            .add(
                hashMapOf(
                    "nombre" to marker.name,
                    "ubicacion" to hashMapOf(
                        "latitude" to marker.position.latitude,
                        "longitude" to marker.position.longitude
                    ),
                    "descripcion" to marker.description,
                    "tipo" to marker.icon,
                    "imagenes" to marker.image,
                    "owner" to marker.owner
                )
            )
    }

    fun getMarkersFromDataBase(): CollectionReference {
        return database.collection("markers")
    }
    fun documentToMarker(document: DocumentSnapshot): CustomMarker {
        val name = document.getString("nombre") ?: ""
        val description = document.getString("descripcion") ?: ""
        val icon = document.getLong("tipo")?.toInt() ?: 0
        val image = document.getString("imagenes") ?: ""
        val positionMap = document.get("ubicacion") as? Map<String, Double>
        val position = LatLng(
            positionMap?.get("latitude") ?: 0.0,
            positionMap?.get("longitude") ?: 0.0
        )
        val owner = document.getString("owner") ?: ""
        return CustomMarker(name, description, position, icon, image, owner)
    }

    fun removeMarker(marker: CustomMarker) {
        database.collection("markers")
            .whereEqualTo("nombre", marker.name)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    database.collection("markers").document(document.id)
                        .delete()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
    }

    fun uploadImage(imageUri: Uri): LiveData<String> {
        val uploadResult = MutableLiveData<String>()
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val filename = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("images/$filename")
        storage.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                uploadResult.value = uri.toString()
            }
        }.addOnFailureListener {
                uploadResult.value = "Error al subir la imagen"
            }
        return uploadResult
    }
    fun deleteImage(imageUri: String): Task<Void> {
        val storage = FirebaseStorage.getInstance().getReferenceFromUrl(imageUri)
        return storage.delete()
    }

    fun getMarkersByType(type: Int): LiveData<List<CustomMarker>> {
        val liveData = MutableLiveData<List<CustomMarker>>()
        database.collection("markers")
            .whereEqualTo("tipo", type)
            .get()
            .addOnSuccessListener { documents ->
                val markers = documents.map { documentToMarker(it) }
                liveData.value = markers
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
        return liveData
    }

}