package com.example.kotik

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ImageItem(val resourceId: Int)

class GreetingActivity : ComponentActivity() {
    private var textUp by mutableStateOf("")
    private var textDown by mutableStateOf("")
    private var isButtonVisible by mutableStateOf(false)

    private var firEllipse: Int by mutableStateOf(R.drawable.ellipse_fill)
    private var secEllipse: Int by mutableStateOf(R.drawable.ellipse_empty)
    private var theEllipse: Int by mutableStateOf(R.drawable.ellipse_empty)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textUp = getString(R.string.greeting_text_up_1)
        textDown = getString(R.string.greeting_text_down_1)

        setContent {
            Greeting()
        }

    }
    @Composable
    fun Greeting() {

        val imageQueue by remember { mutableStateOf(mutableListOf<ImageItem>()) }
        var textNum by remember {mutableStateOf(1)}
        var currentImageIndex by remember { mutableStateOf(0) }

        fun enqueueImage(resourceId: Int) {
            val imageItem = ImageItem(resourceId)
            imageQueue.add(imageItem)
            if (imageQueue.size == 1) {
                currentImageIndex = 0
            }
        }

        fun nextImage() {
            if (currentImageIndex < imageQueue.size - 1) {
                currentImageIndex++
            }
        }

        fun previousImage() {
            if (currentImageIndex > 0) {
                currentImageIndex--
            }
        }

        fun swap(pageNum: Int) {
            when (pageNum) {
                1 -> {
                    textUp = getString(R.string.greeting_text_up_1)
                    textDown = getString(R.string.greeting_text_down_1)
                    isButtonVisible = false
                    firEllipse = R.drawable.ellipse_fill
                    secEllipse = R.drawable.ellipse_empty
                    theEllipse = R.drawable.ellipse_empty
                }
                2 -> {
                    textUp = getString(R.string.greeting_text_up_2)
                    textDown = getString(R.string.greeting_text_down_2)
                    isButtonVisible = false
                    firEllipse = R.drawable.ellipse_empty
                    secEllipse = R.drawable.ellipse_fill
                    theEllipse = R.drawable.ellipse_empty
                }
                3 -> {
                    textUp = getString(R.string.greeting_text_up_3)
                    textDown = getString(R.string.greeting_text_down_3)
                    isButtonVisible = true
                    firEllipse = R.drawable.ellipse_empty
                    secEllipse = R.drawable.ellipse_empty
                    theEllipse = R.drawable.ellipse_fill
                }
            }

        }
        enqueueImage(R.drawable.pana1)
        enqueueImage(R.drawable.pana2)
        enqueueImage(R.drawable.pana3)

        Box(
            modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 40.dp)
                        .offset(y = -50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                            .size(300.dp)
                            .padding(bottom = 5.dp),
                    imageVector = ImageVector.vectorResource(
                            imageQueue.getOrNull(currentImageIndex)?.resourceId ?: R.drawable.pana1),
                    contentDescription = null
                )
                Text(
                    text = textUp,
                    color = Color(android.graphics.Color.parseColor("#0560FA")),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 75.dp, end = 75.dp)
                )
                Text(
                    text = textDown,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 75.dp, end = 75.dp)
                )
            }
            Column(
                modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 75.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                        modifier = Modifier
                                .padding(bottom = 15.dp)
                ) {
                    Image(
                            painter = painterResource(id = firEllipse),
                            contentDescription = null,
                            modifier = Modifier.padding(end = 5.dp)
                    )
                    Image(
                            painter = painterResource(id = secEllipse),
                            contentDescription = null,
                    )
                    Image(
                            painter = painterResource(id = theEllipse),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 5.dp)
                    )
                }
                Column (
                    modifier = Modifier
                            .alpha(if (isButtonVisible) 1f else 0f)
                ) {
                    Button(
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 30.dp, end = 30.dp),
                            colors = ButtonDefaults.buttonColors(
                                    Color(android.graphics.Color.parseColor("#0560FA"))),
                            shape = MaterialTheme.shapes.small,
                            onClick = {
                                if (isButtonVisible) {
                                    getSharedPreferences("firstUserData", Context.MODE_PRIVATE)
                                            .edit().putBoolean("isUserFirstTime", false).apply()
                                    startActivity(Intent(this@GreetingActivity, RegistrationActivity::class.java))
                                    finish()
                                }
                            }
                    ) {
                        Text(
                                "Sign Up",
                                fontSize = 16.sp
                        )
                    }
                    Row (
                            modifier = Modifier
                                    .padding(top = 15.dp)
                                    .align(Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                                text = "Already have an account?",
                                color = Color(android.graphics.Color.parseColor("#A7A7A7"))
                        )
                        Text(
                                modifier = Modifier
                                        .clickable {
                                            getSharedPreferences("firstUserData", Context.MODE_PRIVATE)
                                                    .edit().putBoolean("isUserFirstTime", false).apply()
                                            startActivity(Intent(this@GreetingActivity, LoginActivity::class.java))
                                            finish()
                                        }
                                        .padding(start = 5.dp),
                                text = "Sign In",
                                color = Color(android.graphics.Color.parseColor("#0560FA"))
                        )
                    }
                }
            }
            Row (
                modifier = Modifier
                        .fillMaxSize()
            ) {
                Button(
                        modifier = Modifier
                                .alpha(0f)
                                .fillMaxHeight(0.82f)
                                .fillMaxWidth(0.5f),
                        onClick = {
                            if (textNum > 1) {
                                textNum--
                                swap(textNum)
                                previousImage()
                            }
                        }
                ) {}
                Button(
                        modifier = Modifier
                                .alpha(0f)
                                .fillMaxHeight(0.82f)
                                .fillMaxWidth(),
                        onClick = {
                            if (textNum < 3) {
                                textNum++
                                swap(textNum)
                                nextImage()
                            }
                        }
                ) {}
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Greeting()
    }

}