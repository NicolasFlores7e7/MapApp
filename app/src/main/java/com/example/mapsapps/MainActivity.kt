package com.example.mapsapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapps.navigations.Routes
import com.example.mapsapps.ui.theme.MapsAppsTheme
import com.example.mapsapps.view.AddMarkerScreen
import com.example.mapsapps.view.LogInScreen
import com.example.mapsapps.view.MapAppDrawer
import com.example.mapsapps.view.MarkerListScreen
import com.example.mapsapps.viewModel.MapsViewModel

class MainActivity : ComponentActivity() {
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

