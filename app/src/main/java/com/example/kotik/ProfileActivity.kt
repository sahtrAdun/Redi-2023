package com.example.kotik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ProfileActivity : ComponentActivity() {

    private val colorBlue = Color(android.graphics.Color.parseColor("#0560FA"))
    private val colorWhite = Color(android.graphics.Color.parseColor("#F2F2F2"))
    private val colorPureWhite = Color(android.graphics.Color.parseColor("#FFFFFF"))
    private val colorGrey = Color(android.graphics.Color.parseColor("#A7A7A7"))
    private val colorRedY = Color(android.graphics.Color.parseColor("#EC8000"))
    private val colorBlack = Color(android.graphics.Color.parseColor("#3A3A3A"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserProfile()
        }
    }
    @Composable
    fun LowBar(currentPage: String = "home") {
        Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                    modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                        modifier = Modifier
                                .width(78.dp)
                                .fillMaxHeight()
                                .weight(1f),
                ) {
                    Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.user_home),
                            tint = if (currentPage == "home") colorBlue else colorGrey,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(
                        modifier = Modifier
                                .width(78.dp)
                                .fillMaxHeight()
                                .weight(1f),
                ) {
                    Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.user_wallet),
                            tint = if (currentPage == "wallet") colorBlue else colorGrey,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(
                        modifier = Modifier
                                .width(78.dp)
                                .fillMaxHeight()
                                .weight(1f),
                ) {
                    Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.user_track),
                            tint = if (currentPage == "track") colorBlue else colorGrey,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(
                        modifier = Modifier
                                .width(78.dp)
                                .fillMaxHeight()
                                .weight(1f),
                ) {
                    Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.user_history),
                            tint = if (currentPage == "history") colorBlue else colorGrey,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(
                        modifier = Modifier
                                .width(78.dp)
                                .fillMaxHeight()
                                .weight(1f),
                ) {
                    Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.user_profile),
                            tint = if (currentPage == "profile") colorBlue else colorGrey,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

    @Composable
    fun UserProfile() {
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp)
        ) {
            Box(
                    modifier = Modifier
                            .padding(start = 25.dp, end = 25.dp, top = 25.dp)
                            .height(34.dp)
                            .fillMaxSize()
                            .background(
                                    color = Color(android.graphics.Color.parseColor("#CFCFCF")),
                                    shape = RoundedCornerShape(4.dp)
                            )
            ) {
                Text(
                        text = "Search services",
                        fontSize = 12.sp,
                        color = colorGrey,
                        modifier = Modifier
                                .padding(start = 10.dp)
                                .align(Alignment.CenterStart)
                )
                Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.search_icon),
                        contentDescription = null,
                        tint = colorGrey,
                        modifier = Modifier
                                .padding(end = 10.dp)
                                .align(Alignment.CenterEnd)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            LazyColumn(
                    modifier = Modifier.padding(start = 25.dp, end = 25.dp)
            ) {
                item {
                    Box(
                            modifier = Modifier
                                    .height(91.dp)
                                    .fillMaxSize()
                                    .background(
                                            color = colorBlue,
                                            shape = RoundedCornerShape(8.dp)
                                    )
                    ) {
                        Row(
                                modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .height(60.dp)
                                        .fillMaxWidth()
                                        .padding(start = 10.dp, end = 10.dp)
                        ) {
                            Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.profile_icon),
                                    contentDescription = null,
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                            .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                        text = "Hello /fullName/", /* TODO */
                                        color = colorPureWhite,
                                        fontSize = 16.sp
                                )
                                Text(
                                        text = "We trust you are having a great time",
                                        fontSize = 12.sp,
                                        color = colorGrey
                                )
                            }
                            Box(
                                    modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                            )
                            Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.button_back),
                                    contentDescription = null,
                                    tint = colorPureWhite,
                                    modifier = Modifier
                                            .align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(39.dp))
                    Column {
                        Text(
                                text = "Special for you",
                                fontSize = 14.sp,
                                color = colorRedY
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        LazyRow {
                            item {
                                Box(
                                        modifier = Modifier
                                                .height(64.dp)
                                                .width(166.dp)
                                                .weight(1f)
                                                .background(
                                                        color = colorBlack,
                                                        shape = RoundedCornerShape(8.dp)
                                                )
                                ) {
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.width(5.dp))
                                Box(
                                        modifier = Modifier
                                                .height(64.dp)
                                                .width(166.dp)
                                                .weight(1f)
                                                .background(
                                                        color = colorBlack,
                                                        shape = RoundedCornerShape(8.dp)
                                                )
                                ) {
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.width(5.dp))
                                Box(
                                        modifier = Modifier
                                                .height(64.dp)
                                                .width(166.dp)
                                                .weight(1f)
                                                .background(
                                                        color = colorBlack,
                                                        shape = RoundedCornerShape(8.dp)
                                                )
                                ) {
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.width(5.dp))
                                Box(
                                        modifier = Modifier
                                                .height(64.dp)
                                                .width(166.dp)
                                                .weight(1f)
                                                .background(
                                                        color = colorBlack,
                                                        shape = RoundedCornerShape(8.dp)
                                                )
                                ) {
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(25.dp))
                        Text(
                                text = "What would you like to do",
                                fontSize = 14.sp,
                                color = colorBlue
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Column {
                            Row {
                                Box(
                                        modifier = Modifier
                                                .width(164.dp)
                                                .height(161.dp)
                                                .background(
                                                        color = Color.Transparent,
                                                        shape = RoundedCornerShape(8.dp)
                                                )
                                ) {
                                    Image(
                                            painter = painterResource(R.drawable.home_send_by_phone),
                                            contentDescription = null
                                    )
                                }
                                Spacer(modifier = Modifier.width(13.dp))
                                Box(
                                        modifier = Modifier
                                                .width(164.dp)
                                                .height(161.dp)
                                                .background(
                                                        color = Color.Transparent,
                                                        shape = RoundedCornerShape(8.dp)
                                                )
                                ) {
                                    Image(
                                            painter = painterResource(R.drawable.home_send_a_package),
                                            contentDescription = null
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(13.dp))
                            Row {
                                Box(
                                        modifier = Modifier
                                                .width(164.dp)
                                                .height(161.dp)
                                                .background(
                                                        color = Color.Transparent,
                                                        shape = RoundedCornerShape(8.dp)
                                                )
                                ) {
                                    Image(
                                            painter = painterResource(R.drawable.home_find_your_wallet),
                                            contentDescription = null
                                    )
                                }
                                Spacer(modifier = Modifier.width(13.dp))
                                Box(
                                        modifier = Modifier
                                                .width(164.dp)
                                                .height(161.dp)
                                                .background(
                                                        color = Color.Transparent,
                                                        shape = RoundedCornerShape(8.dp)
                                                )
                                ) {
                                    Image(
                                            painter = painterResource(R.drawable.home_tech_support),
                                            contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        LowBar("home")
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        UserProfile()
    }
}