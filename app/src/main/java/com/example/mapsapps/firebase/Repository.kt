package com.example.mapsapps.firebase


import android.util.Log
import com.example.mapsapps.models.CustomMarker
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Base64
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
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
//                "imagenes" to marker.image
                )
            )
    }

    fun getMarkersFromDataBase(): CollectionReference {
        return database.collection("markers")
    }

    //    fun editMarker(marker: CustomMarker){
//        database.collection("markers")
//            .whereEqualTo("nombre",marker.name)
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    database.collection("markers").document(document.id)
//                        .update(hashMapOf(
//                            "nombre" to marker.name,
//                            "ubicacion" to hashMapOf(
//                                "latitude" to marker.position.latitude,
//                                "longitude" to marker.position.longitude
//                            ),
//                            "descripcion" to marker.position,
//                            "tipo" to marker.icon,
////                            "imagenes" to marker.image
//                        ))
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w("Error", "Error getting documents: ", exception)
//            }
//    }
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

//    fun uploadImage() {
//        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
//        val now = Date()
//        val filename = formatter.format(now)
//        val storage = FirebaseStorage.getInstance().getReference("images/$filename")
//        storage.putFile(imageUri).addOnSuccessListener {
//            binding.imageView.setImageURI(null)
//            Toast.makeText(requireContext(), "Imagen subida correctamente", Toast.LENGTH_SHORT)
//                .show()
//        }
//            .addOnFailureListener {}
//        Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
//    }
//}

//fun getImageUrl(imageName: String): Task<DocumentSnapshot> {
//    val database = FirebaseFirestore.getInstance()
//    val imageRef = database.collection("images").document(imageName)
//    return imageRef.get().addOnFailureListener { exception ->
//        println("Error al obtener el documento: ${exception.message}")
//    }
//}

}