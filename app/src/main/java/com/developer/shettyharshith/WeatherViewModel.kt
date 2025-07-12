package com.developer.shettyharshith

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.shettyharshith.api.Constant
import com.developer.shettyharshith.api.NetworkResponse
import com.developer.shettyharshith.api.RetrofitInstance
import com.developer.shettyharshith.api.WeatherModel
import kotlinx.coroutines.launch

/*----------------------------------------------------------------------------------------------------------------------*/
/*File: WeatherViewModel.kt
Purpose
This file contains the WeatherViewModel class, which is responsible for
managing the data and business logic of the weather app.
It fetches weather data from the API and exposes it to the UI in a way that the UI can observe and react to changes.*//*----------------------------------------------------------------------------------------------------------------------*/


class WeatherViewModel : ViewModel() {

    /*-----------------------------------------------------------------------------------------------------------------------*//*ViewModel:
    The WeatherViewModel extends ViewModel, which is part of Android's architecture components.
    It is designed to survive configuration changes (like screen rotations) and manage UI-related data.*//*-----------------------------------------------------------------------------------------------------------------------*/

    private val weatherApi = RetrofitInstance.weatherApi

    private val _weatherResult =
        MutableLiveData<NetworkResponse<WeatherModel>>()
    /*-----------------------------------------------------------------------------------------------------------------------*/
    /*LiveData:_weatherResult is a MutableLiveData that holds the current state of the weather data (loading, success, or error).*/
    /*-----------------------------------------------------------------------------------------------------------------------*/

    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult
    /*-----------------------------------------------------------------------------------------------------------------------*/
    /* weatherResult is an immutable LiveData that the UI observes to react to changes in the weather data. */
    /*-----------------------------------------------------------------------------------------------------------------------*/

    fun getData(city: String) {

        /*-----------------------------------------------------------------------------------------------------------------------*/
        /* getData(city: String):
        This function fetches weather data for a given city using the API.
        Why? It encapsulates the logic for making the API call, handling success and error cases, and updating the LiveData. This keeps the UI code clean and focused on rendering.*/
        /*-----------------------------------------------------------------------------------------------------------------------*/

        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {

            /*-----------------------------------------------------------------------------------------------------------------------*/
            /*viewModelScope:
            This is a coroutine scope tied to the ViewModel. It ensures that any background work (like API calls) is canceled when the ViewModel is cleared.*/
            /*-----------------------------------------------------------------------------------------------------------------------*/

            try {

                /*-----------------------------------------------------------------------------------------------------------------------*/
                /*Error Handling:
                The try-catch block ensures that any exceptions during the API call are caught, and the UI is notified of the error.*/
                /*-----------------------------------------------------------------------------------------------------------------------*/

                val response = weatherApi.getWeather(Constant.apiKey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to Load Data")
                }
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Failed to Load Data")
            }
        }

    }
}
/*-----------------------------------------------------------------------------------------------------------------------*/
/*
Why This File Exists
The WeatherViewModel separates the data-fetching logic from the UI. This follows the MVVM (Model-View-ViewModel) architecture,
which improves code maintainability and testability. The LiveData ensures that the UI automatically updates when the data changes.*/
/*-----------------------------------------------------------------------------------------------------------------------*/