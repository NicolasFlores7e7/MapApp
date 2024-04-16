package com.example.mapsapps.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsapps.R
import com.example.mapsapps.models.CustomMarker
import com.example.mapsapps.navigations.Routes
import com.example.mapsapps.viewModel.MapsViewModel
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailedScreen(mapsViewModel: MapsViewModel, navController: NavController) {
    val selectedMarker = mapsViewModel.selectedMarker.value
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.9f)
                .clip(RoundedCornerShape(32.dp))
                .border(2.dp, Color(0xFF03045e), RoundedCornerShape(32.dp))
                .background(Color(0xFF8FDEED)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                selectedMarker?.let {
                    Text(
                        text = "Nombre del marcador: ",
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        color = Color(0xFF03045e),
                    )

                    Text(
                        text = it.name,
                        lineHeight = 20.sp,
                        fontSize = 16.sp,
                        color = Color(0xFF03045e),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Descripción del marcador: ",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        color = Color(0xFF03045e)
                    )
                    Text(
                        text = it.description,
                        lineHeight = 20.sp,
                        fontSize = 16.sp,
                        color = Color(0xFF03045e),

                        )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Posición del marcador: ",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        color = Color(0xFF03045e)
                    )
                    Text(
                        text = it.position.toString().replace("lat/lng: ", ""),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        fontSize = 16.sp,
                        color = Color(0xFF03045e),
                    )
                    AsyncImage(
                        model = it.image,
                        contentDescription = "photo",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .width(200.dp),
                        alignment = Alignment.Center,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly

                    ) {
                        Button(
                            modifier = Modifier
                                .padding(top = 8.dp, start = 16.dp),
                            onClick = {
                                navController.navigate(Routes.MarkerList.route)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFcaf0f8), contentColor = Color(0xFF03045e)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Volver")
                        }
                        Button(
                            modifier = Modifier
                                .padding(top = 8.dp, start = 16.dp),
                            onClick = {
                                mapsViewModel.deleteMarker(selectedMarker)
                                navController.navigate(Routes.MarkerList.route)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFcaf0f8), contentColor = Color(0xFF03045e)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Eliminar")
                        }


                    }
                    Button(
                        modifier = Modifier
                            .padding(top = 8.dp, start = 16.dp),
                        onClick = {
                            mapsViewModel.setEditDialogOpener(true)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFcaf0f8), contentColor = Color(0xFF03045e)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Modificar")
                    }
                }
            }
        }
    }
//    if (selectedMarker != null) {
//        EditAlertDialog(selectedMarker, mapsViewModel, navController)
//    }
}

//@Composable
//fun EditAlertDialog(
//    marker: CustomMarker,
//    mapsViewModel: MapsViewModel,
//    navController: NavController
//) {
//    val dialogEditOpener by mapsViewModel.editDialogOpener.observeAsState(false)
//    val iconsList = mapsViewModel.iconsList
//    val context = LocalContext.current
//    val name by mapsViewModel.markerName.observeAsState("")
//    val snippet by mapsViewModel.markerDescription.observeAsState("")
//    if (dialogEditOpener) {
//
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            Dialog(onDismissRequest = { mapsViewModel.setOpenerDialog(false) }) {
//
//                Surface(
//                    color = Color(0xFF90e0ef),
//                    shape = RoundedCornerShape(8.dp),
//                ) {
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        TextField(
//                            value = marker.name,
//                            onValueChange = { mapsViewModel.setMarkerName(it) },
//                            placeholder = {
//                                Text(
//                                    "Nombre del marcador", color = Color(0xFF03045e)
//                                )
//                            },
//                            colors = TextFieldDefaults.colors(
//                                cursorColor = Color(0xFF03045e),
//                                focusedIndicatorColor = Color(0xFFcaf0f8),
//                                unfocusedIndicatorColor = Color(0xFFcaf0f8),
//                                unfocusedContainerColor = Color(0xFFcaf0f8),
//                                focusedContainerColor = Color(0xFFcaf0f8),
//                                focusedTextColor = Color(0xFF03045e),
//                            ),
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                        Spacer(modifier = Modifier.padding(4.dp))
//
//                        TextField(
//                            value = marker.description,
//                            onValueChange = { mapsViewModel.setMarkerDescription(it) },
//                            placeholder = {
//                                Text(
//                                    "Descripción del marcador", color = Color(0xFF03045e)
//                                )
//                            },
//                            colors = TextFieldDefaults.colors(
//                                cursorColor = Color(0xFF03045e),
//                                focusedIndicatorColor = Color(0xFFcaf0f8),
//                                unfocusedIndicatorColor = Color(0xFFcaf0f8),
//                                unfocusedContainerColor = Color(0xFFcaf0f8),
//                                focusedContainerColor = Color(0xFFcaf0f8),
//                                focusedTextColor = Color(0xFF03045e),
//                            )
//                        )
//
//                        Spacer(modifier = Modifier.padding(8.dp))
//
//                        Row {
//                            iconsList.forEach { icon ->
//                                Box(
//                                    modifier = Modifier
//                                        .padding(8.dp)
//                                        .clip(RoundedCornerShape(8.dp))
//                                ) {
//                                    Image(painter = painterResource(id = icon),
//                                        contentDescription = "icon",
//                                        modifier = Modifier
//                                            .size(50.dp)
//                                            .clickable {
//                                                mapsViewModel.setIconNum(iconsList.indexOf(icon))
//                                            })
//                                }
//                            }
//                        }
//                        CameraScreen(navController, mapsViewModel)
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Button(
//                            modifier = Modifier
//                                .padding(bottom = 16.dp)
//                                .fillMaxWidth(), onClick = {
//
//                                mapsViewModel.setEditDialogOpener(false)
//
//                                val bitmap =
//                                    mapsViewModel.photoTaken.value?.asImageBitmap()?.asAndroidBitmap()
//
//                                val baos = ByteArrayOutputStream()
//                                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//                                val data = baos.toByteArray()
//
//                                val imageUri = byteArrayToUri(context, data)
//
//                                val uploadResult = mapsViewModel.addPhotoToRepo(imageUri)
//
//                                uploadResult.observe(context as LifecycleOwner) { imageUrl ->
//                                    if (imageUrl != null) {
//                                        val newMarker = CustomMarker(
//                                            name = name,
//                                            description = snippet,
//                                            position = marker.position,
//                                            icon = iconsList[mapsViewModel.iconNum.value!!],
//                                            image = imageUrl,
//                                            owner = mapsViewModel.userId.value!!
//                                        )
//
//                                        mapsViewModel.editMarker(marker, newMarker)
//                                        mapsViewModel.setSelectedMarker(newMarker)
//                                        navController.navigate(Routes.DetailScreen.route)
//                                    }
//                                }
//                            }, colors = ButtonDefaults.buttonColors(
//                                containerColor = Color(0xFFcaf0f8), contentColor = Color(0xFF03045e)
//                            ), shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Text(text = "Modificar marcador")
//                        }
//                        Spacer(modifier = Modifier.padding(8.dp))
//                    }
//
//                }
//            }
//        }
//
//    }
//}
