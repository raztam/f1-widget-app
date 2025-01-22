package com.example.f1widgetapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.room.AppDatabase
import com.example.f1widgetapp.ui.theme.F1WidgetAppTheme
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.f1widgetapp.data.api.Api
import com.example.f1widgetapp.data.repository.Repository
import com.example.f1widgetapp.viewmodels.DriversViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val db = remember { AppDatabase.getDatabase(context) }
            //val repository = remember { Repository(driverDao = db.driverDao(), remoteDataSource = Api()) }
            
            //val drivers = driverViewModel.driversState.collectAsState()
            LaunchedEffect(key1 = Unit) { // Call fetchDrivers() once
                //driverViewModel.fetchDrivers()
            }
            F1WidgetAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var selectedDriver by remember { mutableStateOf<Driver?>(null) }

                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Select your favorite driver",
                            modifier = Modifier.padding(16.dp)
                        )

                        //DriverDropDown(
                        //    drivers = drivers.value,
                        //    selectedDriver = selectedDriver,
                        //    onDriverSelected = { driver: Driver ->
                        //        selectedDriver = driver
                        //    }
                        //)

                    }
                }
            }
        }
    }
}