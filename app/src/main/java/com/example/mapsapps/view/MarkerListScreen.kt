package com.example.mapsapps.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsapps.models.CustomMarker
import com.example.mapsapps.navigations.Routes
import com.example.mapsapps.viewModel.MapsViewModel

@Composable
fun MarkerListScreen(navController: NavController, mapsViewModel: MapsViewModel) {
    MarkerListItem(mapsViewModel, navController)
}

@Composable
fun MarkerListItem(mapsViewModel: MapsViewModel, navController: NavController) {
    val markers: List<CustomMarker> by mapsViewModel.markers.observeAsState(listOf())
    val iconList = mapsViewModel.iconsList
    Column(
        modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
        ) {
            items(markers.size) {
                val icon = when (markers[it].icon) {
                    iconList[0] -> iconList[0]
                    iconList[1] -> iconList[1]
                    iconList[2] -> iconList[2]
                    iconList[3] -> iconList[3]
                    iconList[4] -> iconList[4]
                    else -> iconList[0]
                }
                MarkerItem(marker = markers[it], icon, mapsViewModel, navController)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MarkerItem(
    marker: CustomMarker, image: Int, mapsViewModel: MapsViewModel, navController: NavController
) {
    Card(border = BorderStroke(
        2.dp, Color(0xFF03045e)
    ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .clickable {
                mapsViewModel.setSelectedMarker(marker)
                navController.navigate(Routes.DetailScreen.route)
            }) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF8FDEED)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, start = 8.dp)
                    .fillMaxWidth(0.3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "icon",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.size(80.dp),
                )

            }
            Column(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {

                Text(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    text = "Nombre del marcador:",
                    textAlign = TextAlign.Start,
                    lineHeight = 20.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    color = Color(0xFF03045e),

                    )
                Text(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    text = marker.name,
                    textAlign = TextAlign.Start,
                    lineHeight = 20.sp,
                    fontSize = 16.sp,
                    color = Color(0xFF03045e),

                    )

                Button(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp)
                        .fillMaxWidth(0.8f),
                    onClick = { mapsViewModel.deleteMarker(marker) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFcaf0f8), contentColor = Color(0xFF03045e)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Eliminar marcador")
                }

            }

        }

    }
}


