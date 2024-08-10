package com.example.locationsample.screen

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.locationsample.ui.theme.LocationSampleTheme


@Composable
fun LocationScreen(
    modifier: Modifier = Modifier,
    gpsLocation: LiveData<Location>,
    wifiLocation: LiveData<Location>,
    onClick: (String) -> Unit = {}
) {
    Column(modifier = modifier) {
        DataView("GPS", gpsLocation)
        DataView("WiFi", wifiLocation)
        GetLocationControl(onClick = onClick)
    }


}

@Composable
fun DataView(
    title: String, location: LiveData<Location>
) {
    val locationValue by location.observeAsState()
    Column(Modifier.padding(8.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleSmall)
        Text(text = "latitude: ${locationValue?.latitude}")
        Text(text = "longitude: ${locationValue?.longitude}")
        Text(text = "altitude: ${locationValue?.altitude}")
        Text(text = "accuracy: ${locationValue?.accuracy}")
    }
}

@Composable
fun GetLocationControl(
    modifier: Modifier = Modifier, onClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ElevatedButton({ onClick("gps") }) {
            Text("GPS")
        }
        Spacer(modifier = Modifier.width(8.dp))
        ElevatedButton({ onClick("wifi") }) {
            Text("WiFi")
        }
    }

}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun LocationScreenPreview(
    modifier: Modifier = Modifier,
) {
    LocationSampleTheme {
        LocationScreen(
            modifier = Modifier.fillMaxSize(),
            MutableLiveData(Location("dummyprovider").apply {
                latitude = 0.0
                longitude = 0.0
            }),
            MutableLiveData(Location("dummyprovider").apply {
                latitude = 0.0
                longitude = 0.0
            }),
        )
    }
}

