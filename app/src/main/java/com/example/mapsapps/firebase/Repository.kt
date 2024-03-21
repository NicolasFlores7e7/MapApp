package com.example.mapsapps.firebase



import android.graphics.Bitmap
import android.util.Log
import com.example.mapsapps.models.CustomMarker
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import com.google.firebase.storage.FirebaseStorage


class Repository {
    private val database=FirebaseFirestore.getInstance()

    fun addMarker(marker: CustomMarker){
        database.collection("markers")
            .add(hashMapOf(
                "nombre" to marker.name,
                "ubicacion" to hashMapOf(
                    "latitude" to marker.position.latitude,
                    "longitude" to marker.position.longitude
                ),
                "descripcion" to marker.description,
                "tipo" to marker.icon,
//                "imagenes" to marker.image
            ))
    }
    fun getMarkersFromDataBase(): CollectionReference {
        return database.collection("markers")
    }
    fun editMarker(marker: CustomMarker){
        database.collection("markers")
            .whereEqualTo("nombre",marker.name)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    database.collection("markers").document(document.id)
                        .update(hashMapOf(
                            "nombre" to marker.name,
                            "ubicacion" to hashMapOf(
                                "latitude" to marker.position.latitude,
                                "longitude" to marker.position.longitude
                            ),
                            "descripcion" to marker.position,
                            "tipo" to marker.icon,
//                            "imagenes" to marker.image
                        ))
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
    }
    fun removeMarker(marker: CustomMarker){
        database.collection("markers")
            .whereEqualTo("nombre",marker.name)
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

    fun uploadImage(bitmap: Bitmap, imageName: String, callback: (String?) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("images/$imageName.jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            // Obtener la URL de descarga de la imagen
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                callback(uri.toString()) // Llamar al callback con la URL de la imagen
            }.addOnFailureListener {
                callback(null) // Si falla, llamar al callback con nulo
            }
        }.addOnFailureListener {
            callback(null) // Si falla, llamar al callback con nulo
        }
    }

}