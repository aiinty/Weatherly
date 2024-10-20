package com.aiinty.weatherly.ui.weather

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aiinty.weatherly.R
import com.aiinty.weatherly.api.NetworkResponse
import com.aiinty.weatherly.api.weathermodel.WeatherModel
import com.aiinty.weatherly.ui.viewmodel.WeatherViewModel

@Preview
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    var location by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherResult.observeAsState()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = location,
                onValueChange = { location = it },
                label = { Text(stringResource(R.string.location)) }
            )

            IconButton(onClick = { viewModel.getWeather(location) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
        }

        Box (
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (val result = weatherResult.value) {
                is NetworkResponse.Failure -> {
                    Text(text = result.message)
                }
                NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }
                is NetworkResponse.Success -> {
                    WeatherDetails(data = result.data)
                }
                null -> { }
            }
        }
    }
}

@Composable
private fun WeatherDetails(
    data: WeatherModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location"
            )
            Text(
                text = "${data.location.name}, ${data.location.country}",
                fontSize = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${data.current.temp_c} °C",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        AsyncImage(
            modifier = Modifier.size(128.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data("https:${data.current.condition.icon}".replace("64x64", "128x128"))
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.current_weather)
        )
        Text(
            text = data.current.condition.text,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column (
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WeatherKeyView(
                        titleId = R.string.humidity,
                        value = data.current.humidity,
                        measurement = " %"
                    )
                    WeatherKeyView(
                        titleId = R.string.uv,
                        value = data.current.uv
                    )
                }
                Column (
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WeatherKeyView(
                        titleId = R.string.wind_speed,
                        measurementId = R.string.km_h,
                        value = data.current.wind_kph
                    )
                    WeatherKeyView(
                        titleId = R.string.precipitation,
                        measurementId = R.string.mm,
                        value = data.current.precip_mm
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherKeyView(
    title: String = "",
    titleId: Int? = null,
    value: String,
    measurement: String = "",
    measurementId: Int? = null,
) {
    var displayTitle = title
    var displayMeasurement = measurement

    if (titleId != null) {
        displayTitle = stringResource(titleId)
    }
    if (measurementId != null) {
        displayMeasurement = stringResource(measurementId)
    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$value$displayMeasurement",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = displayTitle,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}