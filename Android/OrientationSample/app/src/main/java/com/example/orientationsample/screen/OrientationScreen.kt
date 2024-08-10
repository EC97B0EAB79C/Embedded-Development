package com.example.orientationsample.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
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
import com.example.orientationsample.ui.theme.OrientationSampleTheme

@Composable
fun OrientationScreen(
    modifier: Modifier = Modifier,
    orientation: LiveData<FloatArray>,
    realTime: Boolean,
    onClick: () -> Unit = {},
    onCheckChanged: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(), verticalArrangement = Arrangement.Top
    ) {
        ActionRow(realTime, onClick, onCheckChanged)
        DataView(orientation)
    }
}

@Composable
fun ActionRow(
    realTime: Boolean = false,
    onClick: () -> Unit = {},
    onCheckChanged: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedButton(onClick = onClick) {
            Text(text = "Update")
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Checkbox(checked = realTime, onCheckedChange = onCheckChanged)
        Text(text = "Realtime")
    }
}

@Composable
fun DataView(
    orientation: LiveData<FloatArray>
) {
    val orientationValue by orientation.observeAsState(initial = floatArrayOf(0F, 0F, 0F))
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Orientation:")
        Text(text = "Azimuth: ${orientationValue[0]}")
        Text(text = "Pitch: ${orientationValue[1]}")
        Text(text = "Roll: ${orientationValue[2]}")
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun OrientationScreenPreview() {
    OrientationSampleTheme {
            OrientationScreen(
                modifier = Modifier.fillMaxSize(),
                orientation = MutableLiveData(floatArrayOf(45.0f, 30.0f, 90.0f)),
                realTime = false,
                onClick = {},
                onCheckChanged = {}
            )
    }
}
