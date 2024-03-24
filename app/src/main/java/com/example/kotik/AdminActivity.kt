package com.example.kotik

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val colorBlue = Color(android.graphics.Color.parseColor("#0560FA"))
val colorWhite = Color(android.graphics.Color.parseColor("#F2F2F2"))
val colorGrey = Color(android.graphics.Color.parseColor("#A7A7A7"))
val colorRedY = Color(android.graphics.Color.parseColor("#EC8000"))
val colorBlack = Color(android.graphics.Color.parseColor("#3A3A3A"))
val colors = arrayOf(colorBlue, colorWhite)

suspend fun getAllUsers(): List<User> {
    return withContext(Dispatchers.IO) {
        supabase.from("users_public").select().decodeList<User>()
    }
}
suspend fun getAllOrders(): List<Order> {
    return withContext(Dispatchers.IO) {
        supabase.from("orders").select().decodeList<Order>()
    }
}

class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdminProfile()
        }
    }
    @Composable
    fun AdminProfile() {
        val composableScope = rememberCoroutineScope()
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp, bottom = 10.dp)
                ) {
                    Box(
                            modifier = Modifier
                                    .height(91.dp)
                                    .width(341.dp)
                                    .background(
                                            color = Color(android.graphics.Color.parseColor("#0560FA")),
                                            shape = RoundedCornerShape(4.dp)
                                    )
                    ) {

                        Image(
                                painter = painterResource(id = R.drawable.profile_icon),
                                contentDescription = null,
                                modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = 10.dp)
                        )
                        Text(
                                text = "Hello Admin",
                                color = Color(android.graphics.Color.parseColor("#FFFFFF")),
                                fontSize = 24.sp,
                                modifier = Modifier
                                        .align(Alignment.Center)
                                        .offset(x = -(25).dp)
                        )
                        Icon(
                                painter = painterResource(id = R.drawable.logout_icon),
                                tint = Color(android.graphics.Color.parseColor("#ED3A3A")),
                                contentDescription = "LogOut",
                                modifier = Modifier
                                        .size(48.dp)
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 15.dp)
                                        .clickable {
                                            getSharedPreferences("UserData", Context.MODE_PRIVATE)
                                                    .edit()
                                                    .putBoolean("isAdmin", false)
                                                    .apply()

                                            getSharedPreferences("firstUserData", Context.MODE_PRIVATE)
                                                    .edit()
                                                    .putBoolean("isUserSignIn", false)
                                                    .apply()

                                            startActivity(Intent(this@AdminActivity, LoginActivity::class.java))
                                            finish()
                                        }
                        )

                    }
                }
            }
            item {
                Row(modifier = Modifier.padding(start = 25.dp, top = 10.dp)) {
                    Text(
                            text = "Admin Panel",
                            fontSize = 14.sp,
                            color = Color(android.graphics.Color.parseColor("#0560FA")),

                            )
                }
                Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                                .fillMaxWidth()

                ) {
                    Row(modifier = Modifier.padding(top = 10.dp)) {
                        Column {
                            var isBoxOnePressed by remember { mutableStateOf(false) }
                            var isBoxTwoPressed by remember { mutableStateOf(false) }
                            Box(
                                    modifier = Modifier
                                            .width(162.dp)
                                            .height(99.dp)
                                            .background(
                                                    color = if (isBoxOnePressed) colors[0] else colors[1],
                                                    shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                isBoxOnePressed = !isBoxOnePressed
                                                setContent {
                                                    AdminOrders()
                                                }
                                            }
                            ) {
                                Column(
                                        Modifier
                                                .fillMaxHeight()
                                                .padding(start = 15.dp),
                                        verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                            painter = painterResource(id = R.drawable.orders_icon),
                                            tint = if (isBoxOnePressed) colors[1] else colors[0],
                                            contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(
                                            text = "Orders",
                                            fontSize = 16.sp,
                                            color = if (isBoxOnePressed) colors[1] else colors[0],
                                    )
                                }
                            }
                            ////
                            Spacer(modifier = Modifier.height(10.dp))
                            ////
                            Box(
                                    modifier = Modifier
                                            .width(162.dp)
                                            .height(99.dp)
                                            .background(
                                                    color = if (isBoxTwoPressed) colors[0] else colors[1],
                                                    shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                isBoxTwoPressed = !isBoxTwoPressed
                                                setContent {
                                                    AdminOrdersHistory()
                                                }
                                            }
                            ) {
                                Column(
                                        Modifier
                                                .fillMaxHeight()
                                                .padding(start = 15.dp),
                                        verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                            painter = painterResource(id = R.drawable.history_icon),
                                            tint = if (isBoxTwoPressed) colors[1] else colors[0],
                                            contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(
                                            text = "History",
                                            fontSize = 16.sp,
                                            color = if (isBoxTwoPressed) colors[1] else colors[0],
                                    )
                                }
                            }
                        }
                        ////
                        Spacer(modifier = Modifier.width(10.dp))
                        ////
                        Column {
                            var isBoxOnePressed by remember { mutableStateOf(false) }
                            var isBoxTwoPressed by remember { mutableStateOf(false) }
                            Box(
                                    modifier = Modifier
                                            .width(162.dp)
                                            .height(99.dp)
                                            .background(
                                                    color = if (isBoxOnePressed) colors[0] else colors[1],
                                                    shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                isBoxOnePressed = !isBoxOnePressed
                                                setContent {
                                                    /* TODO */
                                                }
                                            }
                            ) {
                                Column(
                                        Modifier
                                                .fillMaxHeight()
                                                .padding(start = 15.dp),
                                        verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                            painter = painterResource(id = R.drawable.support_icon),
                                            tint = if (isBoxOnePressed) colors[1] else colors[0],
                                            contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(
                                            text = "Support",
                                            fontSize = 16.sp,
                                            color = if (isBoxOnePressed) colors[1] else colors[0],
                                    )
                                }
                            }
                            ////
                            Spacer(modifier = Modifier.height(10.dp))
                            ////
                            Box(
                                    modifier = Modifier
                                            .width(162.dp)
                                            .height(99.dp)
                                            .background(
                                                    color = if (isBoxTwoPressed) colors[0] else colors[1],
                                                    shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                isBoxTwoPressed = !isBoxTwoPressed
                                                setContent {
                                                    ReviewList()
                                                }
                                            }
                            ) {
                                Column(
                                        Modifier
                                                .fillMaxHeight()
                                                .padding(start = 15.dp),
                                        verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                            painter = painterResource(id = R.drawable.reviews_icon),
                                            tint = if (isBoxTwoPressed) colors[1] else colors[0],
                                            contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(
                                            text = "Reviews",
                                            fontSize = 16.sp,
                                            color = if (isBoxTwoPressed) colors[1] else colors[0],
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AdminOrders() {
        val composableScope = rememberCoroutineScope()
        val orders = remember { mutableStateListOf<Order>() }
        val users = remember { mutableStateListOf<User>() }


        fun getUserDataById(id: Int, da_ta: String): String {
            val res = users.find { it.id == id }
            return when (da_ta) {
                "name" -> res?.fullName ?: "no name"
                "email" -> res?.email ?: "no email"
                "phone" -> res?.phone ?: "no phone"
                "balance" -> res?.balance.toString() ?: "0"
                else -> "error"
            }
        }

        LaunchedEffect(Unit) {
            users.addAll(getAllUsers())
            orders.addAll(getAllOrders())
        }

        Column(
                Modifier
                        .fillMaxSize()
        ) {
            Row(
                    Modifier
                            .fillMaxWidth()
                            .height(24.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.button_back),
                        tint = colorBlue,
                        contentDescription = null,
                        modifier = Modifier
                                .padding(start = 15.dp)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    setContent {
                                        AdminProfile()
                                    }
                                }
                )
                Text(
                        text = "Orders Status Updates",
                        fontSize = 16.sp,
                        color = colorGrey,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                                .weight(1f)
                                .offset(x = (-15).dp)
                                .align(Alignment.CenterVertically)
                )
            }
            LazyColumn(
                    Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, start = 25.dp, end = 25.dp, bottom = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(orders.filter {!it.succesed}) {order ->
                    ListItem(headlineContent = {
                        Box(
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                        .width(341.dp)
                                        .height(134.dp)
                                        .shadow(1.dp)
                                        .clickable {
                                            setContent {
                                                ThisOrder(order.id)
                                            }
                                        }
                        ) {
                            Column(
                                    Modifier
                                            .fillMaxSize()
                                            .padding(top = 10.dp, start = 25.dp, end = 25.dp, bottom = 10.dp)
                            ) {
                                Text(
                                        text = "${order.id}",
                                        color = colorRedY,
                                        fontSize = 16.sp
                                )
                                Text(
                                        text = "Status: ${when(order.status.toInt()){
                                            0 -> "Order has been placed"
                                            1 -> "Package is at the point of dispatch"
                                            2 -> "Package delivered to the receiving point"
                                            else -> "hz"
                                        }}",
                                        fontSize = 12.sp
                                )
                                Text(
                                        text = "User: ${order.id_user} (${getUserDataById(order.id_user, "name")})",
                                        color = colorGrey,
                                        fontSize = 12.sp
                                )
                                Row(
                                        verticalAlignment = Alignment.Bottom,
                                        modifier = Modifier
                                                .fillMaxSize()
                                                .padding(bottom = 10.dp)
                                ) {
                                    Button(
                                            modifier = Modifier
                                                    .width(108.dp)
                                                    .height(30.dp),
                                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                                            shape = RoundedCornerShape(8.dp),
                                            border = BorderStroke(1.dp, colorBlue),
                                            onClick = { if (order.status.toInt() > 0) {
                                                composableScope.launch {
                                                    withContext(Dispatchers.IO) {
                                                        supabase.from("orders").update(mapOf("status" to order.status - 1)) {
                                                            filter {
                                                                eq("id", order.id)
                                                            }
                                                        }
                                                    }
                                                }
                                            } }
                                    ) {
                                        Text(
                                                text = "Reduce Status",
                                                color = colorBlue,
                                                fontSize = 12.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Button(
                                            modifier = Modifier
                                                    .width(108.dp)
                                                    .height(30.dp),
                                            colors = ButtonDefaults.buttonColors(colorBlue),
                                            shape = RoundedCornerShape(8.dp),
                                            onClick = { if (order.status.toInt() < 2) {
                                                composableScope.launch {
                                                    withContext(Dispatchers.IO) {
                                                        supabase.from("orders").update(mapOf("status" to order.status + 1)) {
                                                            filter {
                                                                eq("id", order.id)
                                                            }
                                                        }
                                                    }
                                                }
                                            } }
                                    ) {
                                        Text(
                                                text = "Increase Status",
                                                color = colorWhite,
                                                fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    @Composable
    fun ThisOrder(id_order: Int) {
        val composableScope = rememberCoroutineScope()
        val orders = remember { mutableStateListOf<Order>() }
        val users = remember { mutableStateListOf<User>() }


        fun getUserDataById(id: Int, da_ta: String): String {
            val res = users.find { it.id == id }
            return when (da_ta) {
                "name" -> res?.fullName ?: "no name"
                "email" -> res?.email ?: "no email"
                "phone" -> res?.phone ?: "no phone"
                "balance" -> res?.balance.toString() ?: "0"
                else -> "error"
            }
        }

        LaunchedEffect(Unit) {
            users.addAll(getAllUsers())
            orders.addAll(getAllOrders())
        }

        Row(
                Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .height(24.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.button_back),
                    tint = colorBlue,
                    contentDescription = null,
                    modifier = Modifier
                            .padding(start = 15.dp)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                setContent {
                                    AdminOrders()
                                }
                            }
            )
            Text(
                    text = "Orders Status Updates",
                    fontSize = 16.sp,
                    color = colorGrey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                            .weight(1f)
                            .offset(x = (-15).dp)
                            .align(Alignment.CenterVertically)
            )
        }
        Column(Modifier
                .fillMaxSize()
                .padding(top = 39.dp)
        ) {
            LazyColumn(Modifier
                    .fillMaxSize()
                    .padding(start = 25.dp, end = 25.dp, top = 15.dp, bottom = 90.dp)
            ) {
                items(orders.filter { it.id == id_order }) {order ->
                    ListItem(headlineContent = {

                        Column {
                            Text(
                                    text = "Delivery Information",
                                    color = colorBlue,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Sender", fontSize = 12.sp)
                            Text(
                                    text = "User: ${id_order}(${getUserDataById(order.id_user, "name")})",
                                    color = colorGrey,
                                    fontSize = 12.sp
                            )
                            Text(
                                    text = "user address", /* TODO */
                                    color = colorGrey,
                                    fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Recipient", fontSize = 12.sp)
                            Text(
                                    text = "${order.country}, ${order.address}",
                                    color = colorGrey,
                                    fontSize = 12.sp
                            )
                            Text(
                                    text = "${order.phone}",
                                    color = colorGrey,
                                    fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Instance", fontSize = 12.sp)
                            Text(
                                    text = "false",
                                    color = colorGrey,
                                    fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Status", fontSize = 12.sp)
                            Text(
                                    text = "Status: ${when(order.status.toInt()){
                                        0 -> "Order has been placed"
                                        1 -> "Package is at the point of dispatch"
                                        2 -> "Package delivered to the receiving point"
                                        else -> "hz"
                                    }}",
                                    color = colorGrey,
                                    fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                    text = "Package Information",
                                    color = colorBlue,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                        text = "Contents",
                                        fontSize = 12.sp,
                                )
                                Spacer(modifier = Modifier.weight(0.6f))
                                Text(
                                        text = "${order.package_items}",
                                        fontSize = 12.sp,
                                        color = colorRedY,
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                        text = "Weight (kg)",
                                        fontSize = 12.sp,
                                )
                                Spacer(modifier = Modifier.weight(0.6f))
                                Text(
                                        text = "${order.weight_items}",/* TODO */
                                        fontSize = 12.sp,
                                        color = colorRedY,
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                        text = "Fragile Items",
                                        fontSize = 12.sp,
                                )
                                Spacer(modifier = Modifier.weight(0.6f))
                                Text(
                                        text = if (order.worth_items > 50) "true" else "false",
                                        fontSize = 12.sp,
                                        color = colorRedY,
                                )
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                            Box(
                                    Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(color = colorBlack)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                    text = "Order Information",
                                    color = colorBlue,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                        text = "Tracking Number",
                                        fontSize = 12.sp,
                                )
                                Spacer(modifier = Modifier.weight(0.6f))
                                Text(
                                        text = "${order.id}",
                                        fontSize = 12.sp,
                                        color = colorRedY,
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                        text = "Order Date",
                                        fontSize = 12.sp,
                                )
                                Spacer(modifier = Modifier.weight(0.6f))
                                Text(
                                        text = "${order.created_at}",
                                        fontSize = 12.sp,
                                        color = colorRedY,
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                        text = "Distance",
                                        fontSize = 12.sp,
                                )
                                Spacer(modifier = Modifier.weight(0.6f))
                                Text(
                                        text = "hz gde brat'",/* TODO */
                                        fontSize = 12.sp,
                                        color = colorRedY,
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                        text = "Delivery Charges",
                                        fontSize = 12.sp,
                                )
                                Spacer(modifier = Modifier.weight(0.6f))
                                Text(
                                        text = "${order.delivery_charges}",
                                        fontSize = 12.sp,
                                        color = colorRedY,
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                        text = "Instant delivery",
                                        fontSize = 12.sp,
                                )
                                Spacer(modifier = Modifier.weight(0.6f))
                                Text(
                                        text = "${order.instant_delivery}",
                                        fontSize = 12.sp,
                                        color = colorRedY,
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                        text = "Tax and Service Charges",
                                        fontSize = 12.sp,
                                )
                                Spacer(modifier = Modifier.weight(0.6f))
                                Text(
                                        text = "${order.tax_and_service_charges}",
                                        fontSize = 12.sp,
                                        color = colorRedY,
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                    Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(color = colorGrey)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                        text = "Total cost",
                                        fontSize = 12.sp,
                                )
                                Spacer(modifier = Modifier.weight(0.6f))
                                Text(
                                        text = "${order.sum_price}",
                                        fontSize = 12.sp,
                                        color = colorRedY,
                                )
                            }
                            Row(
                                    verticalAlignment = Alignment.Bottom,
                                    modifier = Modifier
                                            .fillMaxSize()
                                            .padding(start = 25.dp, end = 25.dp, bottom = 45.dp)
                            ) {
                                Button(
                                        modifier = Modifier
                                                .width(165.dp)
                                                .height(46.dp),
                                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, colorBlue),
                                        onClick = { if (order.status.toInt() > 0) {
                                            composableScope.launch {
                                                withContext(Dispatchers.IO) {
                                                    supabase.from("orders").update(mapOf("status" to order.status - 1)) {
                                                        filter {
                                                            eq("id", order.id)
                                                        }
                                                    }
                                                }
                                            }
                                        } }
                                ) {
                                    Text(
                                            text = "Reduce Status",
                                            color = colorBlue,
                                            fontSize = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Button(
                                        modifier = Modifier
                                                .width(165.dp)
                                                .height(46.dp),
                                        colors = ButtonDefaults.buttonColors(colorBlue),
                                        shape = RoundedCornerShape(8.dp),
                                        onClick = { if (order.status.toInt() > 0) {
                                            composableScope.launch {
                                                withContext(Dispatchers.IO) {
                                                    supabase.from("orders").update(mapOf("status" to order.status + 1)) {
                                                        filter {
                                                            eq("id", order.id)
                                                        }
                                                    }
                                                }
                                            }
                                        } }
                                ) {
                                    Text(
                                            text = "Increase Status",
                                            color = colorWhite,
                                            fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    })
                }
            }
        }

    }

    @Composable
    fun AdminOrdersHistory() {
        val orders = remember { mutableStateListOf<Order>() }

        suspend fun getOrders() {
            withContext(Dispatchers.IO) {
                val res = supabase.from("orders").select().decodeList<Order>()
                orders.addAll(res)
            }
        }
        Column(
                Modifier
                        .fillMaxSize()
        ) {
            Row(
                    Modifier
                            .fillMaxWidth()
                            .height(24.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.button_back),
                        tint = colorBlue,
                        contentDescription = null,
                        modifier = Modifier
                                .padding(start = 15.dp)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    setContent {
                                        AdminProfile()
                                    }
                                }
                )
                Text(
                        text = "Orders History",
                        fontSize = 16.sp,
                        color = colorGrey,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                                .weight(1f)
                                .offset(x = (-15).dp)
                                .align(Alignment.CenterVertically)
                )
            }
            LazyColumn(
                    Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, start = 25.dp, end = 25.dp, bottom = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(orders) {order ->
                    ListItem(headlineContent = {
                        Box(
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                        .width(341.dp)
                                        .height(134.dp)
                                        .shadow(8.dp)
                                        .clickable {
                                            setContent {
                                                ThisOrderHistory()
                                            }
                                        }
                        ) {
                            Column(
                                    Modifier
                                            .fillMaxSize()
                                            .padding(top = 10.dp, start = 25.dp, end = 25.dp, bottom = 10.dp)
                            ) {
                                Text(
                                        text = "",
                                        color = colorRedY,
                                        fontSize = 16.sp
                                )
                                Text(
                                        text = "Status: ",
                                        fontSize = 12.sp
                                )
                                Text(
                                        text = "User: ",
                                        color = colorGrey,
                                        fontSize = 12.sp
                                )
                            }
                        }
                    })
                }
            }
        }
    }

    @Composable
    fun ThisOrderHistory() {
        Row(
                Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .height(24.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.button_back),
                    tint = colorBlue,
                    contentDescription = null,
                    modifier = Modifier
                            .padding(start = 15.dp)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                setContent {
                                    AdminOrdersHistory()
                                }
                            }
            )
            Text(
                    text = "Orders History",
                    fontSize = 16.sp,
                    color = colorGrey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                            .weight(1f)
                            .offset(x = (-15).dp)
                            .align(Alignment.CenterVertically)
            )
        }
        Column(Modifier
                .fillMaxSize()
                .padding(top = 39.dp)
        ) {
            LazyColumn(Modifier
                    .fillMaxSize()
                    .padding(start = 25.dp, end = 25.dp, top = 15.dp, bottom = 90.dp)
            ) {
                item {
                    Column {
                        Text(
                                text = "Delivery Information",
                                color = colorBlue,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Sender",fontSize = 12.sp)
                        Text(
                                text = "User: id(fullName)"/* TODO */,
                                color = colorGrey,
                                fontSize = 12.sp
                        )
                        Text(
                                text = "country, city, address (from)"/* TODO */,
                                color = colorGrey,
                                fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Recipient",fontSize = 12.sp)
                        Text(
                                text = "country, city, address (to)"/* TODO */,
                                color = colorGrey,
                                fontSize = 12.sp
                        )
                        Text(
                                text = "phone"/* TODO */,
                                color = colorGrey,
                                fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Instance",fontSize = 12.sp)
                        Text(
                                text = "true/false"/* TODO */,
                                color = colorGrey,
                                fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Status",fontSize = 12.sp)
                        Text(
                                text = "status from 1 to 4 - text"/* TODO */,
                                color = colorGrey,
                                fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                                text = "Package Information",
                                color = colorBlue,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                    text = "Contents",
                                    fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.weight(0.6f))
                            Text(
                                    text = "content"/* TODO */,
                                    fontSize = 12.sp,
                                    color = colorRedY,
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                    text = "Weight (kg)",
                                    fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.weight(0.6f))
                            Text(
                                    text = "weight"/* TODO */,
                                    fontSize = 12.sp,
                                    color = colorRedY,
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                    text = "Fragile Items",
                                    fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.weight(0.6f))
                            Text(
                                    text = "true/false"/* TODO */,
                                    fontSize = 12.sp,
                                    color = colorRedY,
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Box(
                                Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(color = colorBlack)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                                text = "Order Information",
                                color = colorBlue,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                    text = "Tracking Number",
                                    fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.weight(0.6f))
                            Text(
                                    text = "id/number"/* TODO */,
                                    fontSize = 12.sp,
                                    color = colorRedY,
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                    text = "Order Date",
                                    fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.weight(0.6f))
                            Text(
                                    text = "created_at"/* TODO */,
                                    fontSize = 12.sp,
                                    color = colorRedY,
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                    text = "Distance",
                                    fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.weight(0.6f))
                            Text(
                                    text = "float(km)"/* TODO */,
                                    fontSize = 12.sp,
                                    color = colorRedY,
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                    text = "Delivery Charges",
                                    fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.weight(0.6f))
                            Text(
                                    text = "float"/* TODO */,
                                    fontSize = 12.sp,
                                    color = colorRedY,
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                    text = "Instant delivery",
                                    fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.weight(0.6f))
                            Text(
                                    text = "float"/* TODO */,
                                    fontSize = 12.sp,
                                    color = colorRedY,
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                    text = "Tax and Service Charges",
                                    fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.weight(0.6f))
                            Text(
                                    text = "float"/* TODO */,
                                    fontSize = 12.sp,
                                    color = colorRedY,
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                                Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(color = colorGrey)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                    text = "Total Cost",
                                    fontSize = 12.sp,
                            )
                            Spacer(modifier = Modifier.weight(0.6f))
                            Text(
                                    text = "sum of all 3 charges"/* TODO */,
                                    fontSize = 12.sp,
                                    color = colorRedY,
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ReviewList() {
        val orders = remember { mutableStateListOf<Order>() }
        var rate: Int = 0

        suspend fun getOrders() {
            withContext(Dispatchers.IO) {
                val res = supabase.from("orders").select().decodeList<Order>()
                orders.addAll(res)
            }
        }
        Column(
                Modifier
                        .fillMaxSize()
        ) {
            Row(
                    Modifier
                            .fillMaxWidth()
                            .height(24.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.button_back),
                        tint = colorBlue,
                        contentDescription = null,
                        modifier = Modifier
                                .padding(start = 15.dp)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    setContent {
                                        AdminProfile()
                                    }
                                }
                )
                Text(
                        text = "Reviews",
                        fontSize = 16.sp,
                        color = colorGrey,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                                .weight(1f)
                                .offset(x = (-15).dp)
                                .align(Alignment.CenterVertically)
                )
            }
            LazyColumn(
                    Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, start = 25.dp, end = 25.dp, bottom = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(orders) {order ->
                    ListItem(headlineContent = {
                        Box(
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                        .width(341.dp)
                                        .height(134.dp)
                                        .shadow(1.dp)
                        ) {
                            Column(
                                    Modifier
                                            .fillMaxSize()
                                            .padding(top = 10.dp, start = 25.dp, end = 25.dp, bottom = 10.dp),
                                    verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                        text = "User: id (fullName)", /* TODO */
                                        color = colorRedY,
                                        fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                        text = "Date: ", /* TODO */
                                        fontSize = 12.sp,
                                        color = colorGrey
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Row {
                                    Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.star),
                                            contentDescription = null,
                                            //tint = if (rate >= 1) colorBlue else colorGrey
                                            tint = colorGrey
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.star),
                                            contentDescription = null,
                                            //tint = if (rate >= 2) colorBlue else colorGrey
                                            tint = colorGrey
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.star),
                                            contentDescription = null,
                                            //tint = if (rate >= 3) colorBlue else colorGrey
                                            tint = colorGrey
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.star),
                                            contentDescription = null,
                                            //tint = if (rate >= 4) colorBlue else colorGrey
                                            tint = colorGrey
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.star),
                                            contentDescription = null,
                                            //tint = if (rate == 5) colorBlue else colorGrey
                                            tint = colorGrey
                                    )
                                }
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                        text = "Read review →",
                                        color = colorBlue,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                                .clickable {
                                                    setContent {
                                                        ThisReview()
                                                    }
                                                }
                                )
                            }
                        }
                    })
                }
            }
        }
    }

    @Composable
    fun ThisReview(rate: Int = 0) {
        Row(
                Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .height(24.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.button_back),
                    tint = colorBlue,
                    contentDescription = null,
                    modifier = Modifier
                            .padding(start = 15.dp)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                setContent {
                                    ReviewList()
                                }
                            }
            )
            Text(
                    text = "Reviews",
                    fontSize = 16.sp,
                    color = colorGrey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                            .weight(1f)
                            .offset(x = (-15).dp)
                            .align(Alignment.CenterVertically)
            )
        }
        Column(Modifier
                .fillMaxSize()
                .padding(top = 39.dp)
        ) {
            LazyColumn(Modifier
                    .fillMaxSize()
                    .padding(start = 25.dp, end = 25.dp, top = 25.dp)
            ) {
                item {
                    Text(
                            text = "User",
                            color = colorBlue,
                            fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                            text = "id (fullName)", /* TODO */
                            fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                            text = "Date",
                            color = colorBlue,
                            fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                            text = "date*", /* TODO */
                            fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(color = colorBlack)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                            text = "Rate",
                            color = colorBlue,
                            fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row {
                        Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.star),
                                contentDescription = null,
                                //tint = if (rate >= 1) colorBlue else colorGrey
                                tint = colorGrey
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.star),
                                contentDescription = null,
                                //tint = if (rate >= 2) colorBlue else colorGrey
                                tint = colorGrey
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.star),
                                contentDescription = null,
                                //tint = if (rate >= 3) colorBlue else colorGrey
                                tint = colorGrey
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.star),
                                contentDescription = null,
                                //tint = if (rate >= 4) colorBlue else colorGrey
                                tint = colorGrey
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.star),
                                contentDescription = null,
                                //tint = if (rate == 5) colorBlue else colorGrey
                                tint = colorGrey
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                            text = "Review Body",
                            color = colorBlue,
                            fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                            text = "review text"
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        AdminProfile()
    }
}