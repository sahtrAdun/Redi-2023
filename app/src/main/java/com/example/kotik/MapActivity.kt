package com.example.kotik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.Error

val yandexKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxsamhpY3ZjZmh1b3N5c2hoemFtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDg2MDYxNjYsImV4cCI6MjAyNDE4MjE2Nn0.Q0kY_4TLumR8nzwfY4dvzRYHnL995hVmUjs5R_aBXls"

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapYan()
        }
        MapKitFactory.setApiKey(yandexKey)
    }
    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    @Composable
    fun MapYan() {
        val context = LocalContext.current
        AndroidView(
                factory = { ctx ->
                    MapView(ctx).apply {
                        mapWindow.map.move(
                                CameraPosition(Point(54.710426, 20.452214), 10.0f, 0.0f, 0.0f)
                        )
                    }
                },
                modifier = Modifier.size(200.dp)
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MapYan()
    }
}

