package com.example.f1widgetapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1widgetapp.data.modals.Driver
import com.example.f1widgetapp.data.repository.RepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DriversViewModel(
    private val repository: RepositoryInterface
) : ViewModel() {
    private val _driversState = MutableStateFlow<List<Driver>>(emptyList())
    val driversState = _driversState.asStateFlow()

    fun fetchDrivers() {
        viewModelScope.launch {
            val fetchedDrivers = repository.getAllDrivers()
            _driversState.value = fetchedDrivers
        }
    }

    // Save the selected driver׳s number to shared preferences
    fun saveSelectedDriver(driver: Driver) {
        repository.saveSelectedDriverNumber(driver.driverNumber)


    }

    // Get the selected driver׳s number from shared preferences
    suspend fun getSelectedDriver(): Driver? {
        return repository.getSelectedDriver()
    }
}