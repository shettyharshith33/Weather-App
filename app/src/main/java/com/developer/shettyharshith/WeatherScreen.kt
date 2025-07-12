package com.developer.shettyharshith

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import coil3.compose.AsyncImage
import com.developer.shettyharshith.api.NetworkResponse
import com.developer.shettyharshith.api.WeatherModel



/*
Purpose
This file contains the UI code for the weather app, built using Jetpack Compose.
It observes the WeatherViewModel and displays the weather data or appropriate loading/error states.

Key Components
/*----------------------------------------------------------------------------------------------------------------------*/
WeatherScreen(viewModel: WeatherViewModel):
This is the main composable function that renders the weather screen.
It observes the weatherResult from the WeatherViewModel and reacts to its state (loading, success, or error).
/*----------------------------------------------------------------------------------------------------------------------*/

Search Bar:
The OutlinedTextField allows the user to input a city name.
The IconButton triggers the getData function in the WeatherViewModel to fetch weather data for the entered city.
Why? This provides a simple and intuitive way for users to search for weather information.

/*----------------------------------------------------------------------------------------------------------------------*/
State Handling:
A when block checks the state of weatherResult:
Loading: Shows a CircularProgressIndicator.
Error: Displays an error message.
Success: Calls WeatherDetails to display the weather data.
Why? This ensures the UI reflects the current state of the data.

/*----------------------------------------------------------------------------------------------------------------------*/
WeatherDetails(data: WeatherModel):
This composable displays detailed weather information, such as temperature, location, and weather conditions.
Why? It organizes the UI into smaller, reusable components, making the code easier to read and maintain.

/*----------------------------------------------------------------------------------------------------------------------*/
WeatherKeyValue(key: String, value: String):
A helper composable to display key-value pairs (e.g., "Humidity: 80%").
Why? It avoids repetitive code and makes the UI consistent.
Why This File Exists
The WeatherScreen file focuses on rendering the UI and interacting with the WeatherViewModel. It adheres to the principle of separation of concerns, where the UI is decoupled from the data-fetching logic.  <hr></hr>
How These Files Work Together

/*----------------------------------------------------------------------------------------------------------------------*/

User Interaction:
The user enters a city name in the search bar and clicks the search button.
This triggers the getData function in the WeatherViewModel.
/*----------------------------------------------------------------------------------------------------------------------*/

Data Fetching:
The WeatherViewModel makes an API call to fetch weather data for the entered city.
It updates the LiveData (_weatherResult) with the result (loading, success, or error).

/*----------------------------------------------------------------------------------------------------------------------*/

UI Update:
The WeatherScreen observes the weatherResult and updates the UI based on its state.
/*----------------------------------------------------------------------------------------------------------------------*/

Why This Approach?
Separation of Concerns:
The WeatherViewModel handles data and business logic.
The WeatherScreen handles UI rendering.

Reactivity:
The use of LiveData ensures that the UI automatically updates when the data changes.

Scalability:
This architecture makes it easy to add new features or modify existing ones without affecting unrelated parts of the code.

/*----------------------------------------------------------------------------------------------------------------------*/

Steps to Integrate API in a New Project
Set Up Retrofit:
Create a Retrofit instance to handle API calls.
Define an interface for your API endpoints.

Create a ViewModel:
Use ViewModel to manage data and business logic.
Use LiveData to expose the data to the UI.


Build the UI:
Use Jetpack Compose (or XML) to create the UI.
Observe the LiveData from the ViewModel to update the UI reactively.


Handle States:
Define states like loading, success, and error to manage the UI's behavior during API calls.
By following this approach, you can build a clean, maintainable, and scalable app.*/


/*----------------------------------------------------------------------------------------------------------------------*/





























@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }

    val weatherResult = viewModel.weatherResult.observeAsState()

    var keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(modifier = Modifier
                .padding(20.dp)
                .weight(1f),
                value = city,
                onValueChange = { city = it },
                placeholder = { Text("Enter the city") },
                label = {
                    Text("Search")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.getData(city)
                        keyboardController?.hide()
                    }) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.Default.Search,
                            tint = Color.DarkGray,
                            contentDescription = "Search"
                        )
                    }
                })
        }
        when (val result = weatherResult.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }

            null -> {
            }
        }
    }
}

@Composable
fun WeatherDetails(data: WeatherModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Icon",
                    modifier = Modifier.size(28.dp)
                )

                Text(
                    text = data.location.name, fontSize = 22.sp, color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(8.dp))

//            Spacer(modifier = Modifier.width(8.dp))
//            Text(text = data.location.region, fontSize = 18.sp, color = Color.DarkGray)
            }
            Text(
                text = data.location.country, fontSize = 18.sp, color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${data.current.temp_c} ° C",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Card(modifier = Modifier.size(160.dp)){
                AsyncImage(
                    modifier = Modifier.size(140.dp),
                    model = "https:${data.current.condition.icon}",
                    contentDescription = "Condition Icon",
                )
            }
            Text(
                text = data.current.condition.text,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        WeatherKeyValue("Humidity", data.current.humidity)
                        WeatherKeyValue("Day", data.current.is_day)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        WeatherKeyValue("Cloud", data.current.cloud)
                        WeatherKeyValue("Feels like", "${data.current.feelslike_c}° C")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        WeatherKeyValue("Wind", "${data.current.gust_kph}KM/Hr")
//                    WeatherKeyValue("Date", data.location.localtime.split("")[1])
                    }
                }
            }
            Spacer(modifier = Modifier.height(35.dp))
        }
    }
}

@Composable
fun WeatherKeyValue(key: String, value: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = key, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = value, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
    }
}