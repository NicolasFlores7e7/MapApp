package com.example.mapsapps.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mapsapps.models.CustomMarker
import com.example.mapsapps.viewModel.MapsViewModel

@Composable
fun MarkerListScreen(navController: NavController, mapsViewModel: MapsViewModel) {
    Text(text = "Marker List")
//    MarkerListItem(mapsViewModel)
}

@Composable
fun MarkerListItem(mapsViewModel: MapsViewModel) {
    val markers: List<CustomMarker> by mapsViewModel.markers.observeAsState(listOf())
    Column(
        modifier = Modifier
        .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
        ) {
            items(markers.size) {
                MarkerItem(marker= markers[it])
            }
        }
    }
}

@Composable
fun MarkerItem(marker: CustomMarker) {
    Card(
        border = BorderStroke(
            2.dp,
            Color.Blue
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                    Text(text = marker.name)
                    Text(text = marker.description)

            }
        }
    }
}


