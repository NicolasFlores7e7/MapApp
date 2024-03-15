package com.example.mapsapps.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.example.mapsapps.models.CustomMarker
import com.example.mapsapps.viewModel.MapsViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapps.navigations.Routes
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

//COLOR PATELLETE https://coolors.co/03045e-0077b6-00b4d8-90e0ef-caf0f8
// RECURSO GUAPO https://proandroiddev.com/mapping-experiences-with-google-maps-and-jetpack-compose-e0cca15c4359
@Composable
fun MapAppDrawer(mapsViewModel: MapsViewModel) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(drawerState = state, gesturesEnabled = state.isOpen, drawerContent = {
        ModalDrawerSheet(drawerContainerColor = Color(0xFF8FDEED),
            modifier = Modifier.clickable { scope.launch { state.close() } }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Location",
                    tint = Color(0xFF03045e)
                )
                Text(
                    "Menú",
                    color = Color(0xFF03045e),
                    fontSize = 40.sp,
                    modifier = Modifier.padding(16.dp)
                )

            }
            NavigationDrawerItem(modifier = Modifier.padding(top = 8.dp), label = {
                Text(
                    text = "Mapa",
                    color = Color(0xFF03045e),
                    fontSize = 20.sp,
                )
            }, selected = false, colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color(0xFFcaf0f8),
            ), shape = RectangleShape, onClick = {
                navController.navigate(Routes.Map.route)
                scope.launch {
                    state.close()
                }
            })
            NavigationDrawerItem(modifier = Modifier.padding(top = 8.dp), label = {
                Text(
                    text = "Lista de marcadores",
                    color = Color(0xFF03045e),
                    fontSize = 20.sp,
                )
            }, selected = false, colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color(0xFFcaf0f8),

                ), shape = RectangleShape, onClick = {
                navController.navigate(Routes.MarkerList.route)
                scope.launch {
                    state.close()
                }
            })

        }
    }) {

        MapAppScafold(state, mapsViewModel, navController)
    }

}


@Composable
fun MapAppScafold(state: DrawerState, mapsViewModel: MapsViewModel, navController: NavController) {

    val showBottomSheet by mapsViewModel.showBottomSheet.observeAsState(false)
    Scaffold(topBar = { MapAppTopBar(state = state) }, bottomBar = { }, content = { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFcaf0f8))
        ) {
            if (showBottomSheet) {
                BottomSheet(
                    onDismiss = { mapsViewModel.showBottomSheet.value = false },
                    mapsViewModel = mapsViewModel
                )
            }

            NavHost(
                navController = navController as NavHostController,
                startDestination = Routes.Map.route
            ) {
                composable(Routes.Login.route) { LogInScreen(navController, mapsViewModel) }
                composable(Routes.AddMarker.route) { AddMarkerScreen(navController, mapsViewModel) }
                composable(Routes.Map.route) { Map(mapsViewModel) }
                composable(Routes.MarkerList.route) {
                    MarkerListScreen(
                        navController, mapsViewModel
                    )
                }
            }
        }

    }


    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapAppTopBar(state: DrawerState) {
    val scope = rememberCoroutineScope()
    TopAppBar(title = {
        Text(
            modifier = Modifier.fillMaxWidth(.8f),
            text = "MapsApp",
            textAlign = TextAlign.Center,
            color = Color(0xFF03045e)
        )
    }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color(0xFF90e0ef),
    ), navigationIcon = {
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

    })
}


@Composable
fun Map(mapsViewModel: MapsViewModel) {
    val markers by mapsViewModel.markers.observeAsState(emptyList())
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

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            GoogleMap(modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings,
                onMapLongClick = {
                    mapsViewModel.showBottomSheet.value = true
                    mapsViewModel.currentLatLng.value = it
                }) {
                markers?.forEach { newMarker ->
                    Marker(
                        state = MarkerState(
                            position = newMarker.position,

                            ),
                        title = newMarker.name,
                        snippet = newMarker.description,
                        icon = resizeMarkerIcon(
                            context = LocalContext.current, icon = newMarker.icon
                        )
                    )
                }

            }
            Switch(modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(vertical = 16.dp, horizontal = 16.dp),
                checked = uiSettings.zoomControlsEnabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF8FDEED),
                    checkedTrackColor = Color(0xFFC8EEF6),
                    uncheckedThumbColor = Color(0xFFC8EEF6),
                    uncheckedTrackColor = Color(0xFF8FDEED)

                ),
                onCheckedChange = {
                    uiSettings = uiSettings.copy(zoomControlsEnabled = it)
                    properties = if (it) {
                        properties.copy(mapType = MapType.NORMAL)
                    } else {
                        properties.copy(mapType = MapType.SATELLITE)
                    }
                })
        }


    }
}

fun resizeMarkerIcon(context: Context, icon: Int): BitmapDescriptor {
    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, icon)
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)
    return BitmapDescriptorFactory.fromBitmap(resizedBitmap)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(onDismiss: () -> Unit, mapsViewModel: MapsViewModel) {
    val modalBotomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBotomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = Color(0xFF8FDFEE),
        modifier = Modifier.fillMaxWidth(),

        ) {
        MarkerCreator(mapsViewModel)
    }

}


@Composable
fun MarkerCreator(mapsViewModel: MapsViewModel) {
    var name by remember { mutableStateOf("") }
    var snippet by remember { mutableStateOf("") }
    val currentLatLng by mapsViewModel.currentLatLng.observeAsState(LatLng(0.0, 0.0))
    val iconsList = mapsViewModel.iconsList

    var selectedIconNum by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        TextField(value = name, onValueChange = { name = it }, placeholder = {
            Text(
                "Nombre del marcador", color = Color(0xFF03045e)
            )
        }, colors = TextFieldDefaults.colors(
            cursorColor = Color(0xFF03045e),
            focusedIndicatorColor = Color(0xFFcaf0f8),
            unfocusedIndicatorColor = Color(
                0xFFcaf0f8
            ),
            unfocusedContainerColor = Color(0xFFcaf0f8),
            focusedContainerColor = Color(0xFFcaf0f8),
            focusedTextColor = Color(0xFF03045e),
        ), shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(value = snippet, onValueChange = { snippet = it }, placeholder = {
            Text(
                "Descripción del marcador", color = Color(0xFF03045e)
            )
        }, colors = TextFieldDefaults.colors(
            cursorColor = Color(0xFF03045e),
            focusedIndicatorColor = Color(0xFFcaf0f8),
            unfocusedIndicatorColor = Color(
                0xFFcaf0f8
            ),
            unfocusedContainerColor = Color(0xFFcaf0f8),
            focusedContainerColor = Color(0xFFcaf0f8),
            focusedTextColor = Color(0xFF03045e),
        )
        )


        Spacer(modifier = Modifier.padding(8.dp))

        Row {
            iconsList.forEach { icon ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))

                ) {
                    Image(painter = painterResource(id = icon),
                        contentDescription = "icon",
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                selectedIconNum = iconsList.indexOf(icon)
                            })
                }
            }
        }
        Button(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth(0.8f), onClick = {
                val newMarker =
                    CustomMarker(name, snippet, currentLatLng, iconsList[selectedIconNum])
                mapsViewModel.addMarker(newMarker)
                mapsViewModel.showBottomSheet.value = false
            }, colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFcaf0f8), contentColor = Color(0xFF03045e)
            ), shape = RoundedCornerShape(8.dp)

        ) {
            Text(text = "Agregar marcador")

        }

    }
}






