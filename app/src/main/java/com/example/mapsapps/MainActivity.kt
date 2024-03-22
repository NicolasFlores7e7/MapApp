package com.example.mapsapps

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mapsapps.ui.theme.MapsAppsTheme
import com.example.mapsapps.view.MapAppDrawer
import com.example.mapsapps.viewModel.MapsViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapsViewModel by viewModels<MapsViewModel>()
        setContent {
            MapsAppsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    MapAppDrawer(mapsViewModel)

                }
            }
        }
    }
}

