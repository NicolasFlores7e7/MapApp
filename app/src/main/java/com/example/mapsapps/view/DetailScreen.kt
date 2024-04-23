package com.example.mapsapps.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mapsapps.navigations.Routes
import com.example.mapsapps.viewModel.MapsViewModel

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
                }
            }
        }
    }
}