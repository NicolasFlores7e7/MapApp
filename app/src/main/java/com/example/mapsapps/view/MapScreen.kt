package com.example.mapsapps.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapps.MainActivity
import com.example.mapsapps.R
import com.example.mapsapps.models.CustomMarker
import com.example.mapsapps.navigations.Routes
import com.example.mapsapps.viewModel.MapsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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

//COLOR PATELLETE https://coolors.co/03045e-0077b6-00b4d8-90e0ef-caf0f8
// RECURSO GUAPO https://proandroiddev.com/mapping-experiences-with-google-maps-and-jetpack-compose-e0cca15c4359
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
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


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
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
                    mapsViewModel = mapsViewModel,
                    navController = navController
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
                composable(Routes.PhotoScreen.route) { PhotoScreen(navController, mapsViewModel) }

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


@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Map(mapsViewModel: MapsViewModel) {
    val markers by mapsViewModel.markers.observeAsState(emptyList())
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    val context = LocalContext.current
    val fusedLocationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }
    var lastKnowLocation by remember { mutableStateOf<android.location.Location?>(null) }
    var deviceLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val cameraPositionState =
        rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(deviceLatLng, 18f) }
    val locationResult = fusedLocationProviderClient.getCurrentLocation(100, null)
    locationResult.addOnCompleteListener(context as MainActivity) { task ->
        if (task.isSuccessful && task.result != null) {
            lastKnowLocation = task.result
            deviceLatLng = LatLng(lastKnowLocation!!.latitude, lastKnowLocation!!.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLatLng, 18f)
        } else {
            Log.e("Error", "Exception: %s", task.exception)
        }
    }



    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    if (permissionState.status.isGranted) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(32.dp))
                .background(Color(0xFF90e0ef))
        ) {

            var uiSettings by remember { mutableStateOf(MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true,
                )) }
            var properties by remember {
                mutableStateOf(MapProperties(
                    mapType = MapType.NORMAL,
                    isMyLocationEnabled = true)
                )
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
                    .align(Alignment.TopStart)
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


    } else {
        Text(text = "Permission required")
    }
}

fun resizeMarkerIcon(context: Context, icon: Int): BitmapDescriptor {
    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, icon)
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)
    return BitmapDescriptorFactory.fromBitmap(resizedBitmap)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    mapsViewModel: MapsViewModel,
    navController: NavController
) {
    val modalBotomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBotomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = Color(0xFF8FDFEE),
        modifier = Modifier
            .fillMaxWidth(),


        ) {
        MarkerCreator(mapsViewModel, navController)
    }

}


@Composable
fun MarkerCreator(mapsViewModel: MapsViewModel, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var snippet by remember { mutableStateOf("") }
    val currentLatLng by mapsViewModel.currentLatLng.observeAsState(LatLng(0.0, 0.0))
    val iconsList = mapsViewModel.iconsList
    val context = LocalContext.current
    val defaultImageBitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.no_image)


    var selectedIconNum by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = {
                Text(
                    "Nombre del marcador",
                    color = Color(0xFF03045e)
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
        Spacer(modifier = Modifier.padding(4.dp))

        TextField(
            value = snippet,
            onValueChange = { snippet = it },
            placeholder = {
                Text(
                    "Descripción del marcador",
                    color = Color(0xFF03045e)
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
        CameraScreen(navController, mapsViewModel)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.8f), onClick = {

                val newMarker =
                    CustomMarker(
                        name,
                        snippet,
                        currentLatLng,
                        iconsList[selectedIconNum],
//                        mapsViewModel.photoTaken.value ?: defaultImageBitmap
                    )
                mapsViewModel.addMarker(newMarker)
                mapsViewModel.showBottomSheet.value = false
            }, colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFcaf0f8), contentColor = Color(0xFF03045e)
            ), shape = RoundedCornerShape(8.dp)

        ) {
            Text(text = "Agregar marcador")

        }
        Spacer(modifier = Modifier.padding(8.dp))


    }
}

@Composable
fun CameraScreen(navController: NavController, mapsViewModel: MapsViewModel) {
    val context = LocalContext.current
    val isCameraPermGranted by mapsViewModel.cameraPermission.observeAsState(false)
    val shouldShowPermRationale by mapsViewModel.shouldPermRationale.observeAsState(false)
    val showPermDenied by mapsViewModel.showPermDenied.observeAsState(false)

    val noImageBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.no_image)
    val photoTaken by mapsViewModel.photoTaken.observeAsState(noImageBitmap)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                mapsViewModel.setCameraPermission(true)
            } else {
                mapsViewModel.setShouldPermRationale(
                    shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.CAMERA
                    )
                )
                if (!shouldShowPermRationale) {
                    Log.i("CameraScreen", "No podemos volver a pedir permisos")
                    mapsViewModel.setShowPermDenied(true)
                }
            }
        }
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    if (!isCameraPermGranted) {
                        launcher.launch(Manifest.permission.CAMERA)
                    } else {
                        navController.navigate(Routes.PhotoScreen.route)
                        mapsViewModel.showBottomSheet.value = false
                    }
                }, modifier = Modifier
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFcaf0f8), contentColor = Color(0xFF03045e)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Take photo")
            }
            Spacer(modifier = Modifier.width(8.dp))
            photoTaken?.let { BitmapPainter(it.asImageBitmap()) }
                ?.let {
                    Image(
                        painter = it,
                        contentDescription = "photo",
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color(0xFFcaf0f8), CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
        }
        if (showPermDenied) {
            PermissionDeclinedScreen()
        }
    }
}

@Composable
fun PermissionDeclinedScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = "Permission required to take photos",
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "This app need access to your camera to take photos"
        )
        Button(onClick = {
            openAppSettings(context as Activity)
        }) {
            Text(text = "Accept")
        }

    }
}

fun openAppSettings(activity: Activity) {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", activity.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    activity.startActivity(intent)
}





