package com.example.mapsapps.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.mapsapps.viewModel.MapsViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

//COLOR PATELLETE https://coolors.co/03045e-0077b6-00b4d8-90e0ef-caf0f8
// RECURSO GUAPO https://proandroiddev.com/mapping-experiences-with-google-maps-and-jetpack-compose-e0cca15c4359
@Composable
fun MapAppDrawer(mapsViewModel: MapsViewModel) {
    val navigationController = rememberNavController()
    val scope = rememberCoroutineScope()
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = state,
        gesturesEnabled = state.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF8FDEED),
                modifier = Modifier
                    .clickable { scope.launch { state.close() } }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        tint = Color(0xFF03045e)
                    )
                    Text(
                        "Marcadores",
                        color = Color(0xFF03045e),
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

                NavigationDrawerItem(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    label = { Text(text = "Marcador 1") },
                    selected = false,
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color(0xFFcaf0f8),

                        ),
                    shape = RectangleShape,
                    onClick = {
                        scope.launch {
                            state.close()
                        }
                    })
            }
        }
    ) {
        MapAppScafold(state, mapsViewModel)
    }

}


@Composable
fun MapAppScafold(state: DrawerState, mapsViewModel: MapsViewModel) {
    Scaffold(
        topBar = { MapAppTopBar(state = state) },
        bottomBar = { },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFcaf0f8))
            ) {
                Map(mapsViewModel)
            }
        }


    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapAppTopBar(state: DrawerState) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth(.8f),
                text = "MapsApp",
                textAlign = TextAlign.Center,
                color = Color(0xFF03045e)
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF90e0ef),
        ),
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    state.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color(0xFF03045e)
                )

            }

        }
    )
}


@Composable
fun Map(mapsViewModel: MapsViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(32.dp))
            .background(Color(0xFF90e0ef))
    ) {
        val itb = LatLng(41.4534265, 2.18375151)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 10f)
        }
        var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }
        var properties by remember {
            mutableStateOf(MapProperties(mapType = MapType.NORMAL))
        }
        val marker by mapsViewModel.markers.observeAsState()
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings,
                onMapLongClick = {
                    mapsViewModel.addMarker(MarkerOptions().position(it))
                    println(it)
                    println("marcadores "+ (mapsViewModel.markers.value?.size ?:0 ))
                }
            ) {
                marker?.forEach {
                    Marker(
                        state = MarkerState(
                            position = it.position
                        ),
                        title = ""
                    )

                }

            }
            Switch(modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(vertical = 16.dp, horizontal = 16.dp),
                checked = uiSettings.zoomControlsEnabled,
                onCheckedChange = {
                    uiSettings = uiSettings.copy(zoomControlsEnabled = it)
                    properties = if (it) {
                        properties.copy(mapType = MapType.NORMAL)
                    } else {
                        properties.copy(mapType = MapType.SATELLITE)
                    }
                }
            )
        }


    }
}