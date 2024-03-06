package com.example.mapsapps.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.mapsapps.viewModel.MapsViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch


@Composable
fun MapAppDrawer(mapsViewModel: MapsViewModel) {
    val navigationController = rememberNavController()
    val scope = rememberCoroutineScope()
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(drawerState = state, gesturesEnabled = false, drawerContent = {
        ModalDrawerSheet(modifier = Modifier
            .clickable { scope.launch { state.close() } }) {
            Text("Marcadores", modifier = Modifier
                .padding(16.dp))
            Divider()
            NavigationDrawerItem(
                label = { Text(text = "Marcador 1") },
                selected = false,
                onClick = {
                    scope.launch {
                        state.close()
                    }
                })
        }
    }) {
        MapAppScafold(state)
    }

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapAppScafold(state: DrawerState) {
    Scaffold(
        topBar = { MapAppTopBar(state = state) },
        bottomBar = { },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Map()
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
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    state.open()
                }
            }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")

            }

        }
    )
}


@Composable
fun Map() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(32.dp))
    ) {
        val itb = LatLng(41.4534265, 2.18375151)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 10f)
        }
        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ){

        }
//        DissaperingScaleBar(
//            modifier = Modifier
//                .align(Alignment.TopStart),
//            cameraPositionState = cameraPositionState
//        )
    }
}