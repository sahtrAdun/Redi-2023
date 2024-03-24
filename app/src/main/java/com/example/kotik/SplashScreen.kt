package com.example.kotik

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.kotik.ui.theme.KotikTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowLogo()
        }
        lifecycleScope.launchWhenCreated {
            delay(1500)

            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        }
    }
}

@Composable
fun ShowLogo() {
    Box(
            modifier = Modifier
                .fillMaxSize()
                .size(100.dp),
            contentAlignment = Alignment.Center
    ) {
        Image(
                imageVector = ImageVector.vectorResource(R.drawable.logo),
                contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LogoPreview() {
    ShowLogo()
}