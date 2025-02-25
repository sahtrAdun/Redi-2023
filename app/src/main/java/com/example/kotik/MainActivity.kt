package com.example.kotik

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

private const val baseUrl = ""
private const val apiKey = ""
val supabase = createSupabaseClient(
        supabaseUrl = baseUrl,
        supabaseKey = apiKey
) {
    install(Postgrest)
    install(Auth) {
        flowType = FlowType.PKCE
        scheme = "com.example.kotik"
        host = "supabase.com"
    }
}
@Serializable
data class User(
        @SerialName("id")
        val id: Int,
        @SerialName("fullName")
        val fullName: String,
        @SerialName("phone")
        val phone: String,
        @SerialName("balance")
        val balance: Double,
        @SerialName("isAdmin")
        val isAdmin: Boolean,
        @SerialName("email")
        val email: String,
        @SerialName("password")
        val password: String
)
@Serializable
data class Order(
        @SerialName("id")
        val id: Int,
        @SerialName("id_user")
        val id_user: Int,
        @SerialName("address")
        val address: String,
        @SerialName("country")
        val country: String,
        @SerialName("phone")
        val phone: String,
        @SerialName("others")
        val others: String,
        @SerialName("package_items")
        val package_items: String,
        @SerialName("weight_items")
        val weight_items: Int,
        @SerialName("worth_items")
        val worth_items: Int,
        @SerialName("created_at")
        val created_at: String,
        @SerialName("payed")
        val payed: Boolean,
        @SerialName("status")
        val status: Short,
        @SerialName("succesed")
        val succesed: Boolean,
        @SerialName("rate")
        val rate: Short,
        @SerialName("feedback")
        val feedback: String,
        @SerialName("delivery_charges")
        val delivery_charges: Double,
        @SerialName("instant_delivery")
        val instant_delivery: Double,
        @SerialName("tax_and_service_charges")
        val tax_and_service_charges: Double,
        @SerialName("sum_price")
        val sum_price: Double

)

class MainActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("firstUserData", Context.MODE_PRIVATE)
        if (getBoolean("isUserFirstTime", true)) {
            startActivity(Intent(this, GreetingActivity::class.java))
            finish()
        }
        if (!getBoolean("isUserSignUp", false)) {
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }
        if (!getBoolean("isUserSignIn", false)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        if (getBoolean("isAdmin", false)) {
            startActivity(Intent(this, AdminActivity::class.java))
            finish()
        }

        setContent {
            MainContent()
        }

    }
    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    private fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    @Composable
    fun MainContent() {
        val users = remember { mutableStateListOf<User>() }

        suspend fun getAllUsers() {
            withContext(Dispatchers.IO) {
                val results = supabase.from("users_public").select().decodeList<User>()
                users.addAll(results)
            }
        }
        LaunchedEffect(Unit) {
            getAllUsers()
        }
        Column(
                modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                        .padding(top = 90.dp)
        ) {
            LazyColumn {
                items(users) { user ->
                    ListItem(headlineContent = {
                        Box(
                                modifier = Modifier
                                        .height(150.dp)
                                        .width(300.dp)
                                        .background(color = Color(android.graphics.Color.parseColor("#A7A7A7")),
                                                shape = MaterialTheme.shapes.medium)
                                        .align(Alignment.CenterHorizontally)
                        ) {
                            Column(modifier = Modifier.padding(top=5.dp,start=5.dp,end=5.dp,bottom=5.dp)) {
                                Text(text = user.id.toString())
                                Text(text = user.fullName?: "")
                                Text(text = user.phone?: "")
                            }
                        }

                    })
                }
            }
        }
        Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
        ) {
            Button(
                    modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 25.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#0560FA"))),
                    onClick = {

                        saveBoolean("isUserSignIn", false)

                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                    }
            ) {
                Text(text = "Logout")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainPreview() {
        MainContent()
    }

}
