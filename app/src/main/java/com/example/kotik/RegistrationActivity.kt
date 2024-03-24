package com.example.kotik

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Registration()
        }
    }
    @Composable
    fun Registration() {
        var newUserFullName by remember { mutableStateOf("") }
        var newUserPhone by remember { mutableStateOf("") }
        var newUserEmail by remember { mutableStateOf("") }
        var newUserPassword by remember { mutableStateOf("") }
        var newUserPasswordConfirm by remember { mutableStateOf("") }
        var isChecked by remember { mutableStateOf(false) }
        val checkedState = remember { mutableStateOf(false) }

        val composableScope = rememberCoroutineScope()

        suspend fun signUp(user_email: String, user_password: String): Boolean {
            var allRight = true
            lifecycleScope.launch {
                try {
                    val user = supabase.auth.signUpWith(Email) {
                        email = user_email
                        password = user_password
                    }
                } catch (e: Exception) {
                    allRight = false
                    Log.e("User_Reg", "Error: ${e.message}", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                                this@RegistrationActivity,
                                "TryCatch_SignUp",
                                Toast.LENGTH_SHORT
                        )
                    }
                }
            }
            return allRight
        }

        suspend fun getIdUser(email: String): String {
            val user = supabase.from("auth.users").select {
                "id"
                filter { eq("email", email) }
            }
            return user.data.get(0).toString()?: ""
        }


        Column(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, start = 25.dp)
        ) {
            Text(
                    text = "Create an account",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
            )
            Text(
                    text = "Complete the sign up process to get started",
                    color = Color(android.graphics.Color.parseColor("#A7A7A7")),
                    fontSize = 14.sp
            )
        }
        LazyColumn(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 25.dp, end = 25.dp, top = 100.dp, bottom = 50.dp),
                contentPadding = PaddingValues(12.dp)
        ) {
            item {
                var isFieldFocused by remember { mutableStateOf(false) }
                OutlinedTextField(
                        value = newUserFullName,
                        onValueChange = { newUserFullName = it },
                        label = { Text("Full Name") },
                        placeholder = { Text("Enter your full name") },
                        singleLine = true,
                        modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { isFieldFocused = it.isFocused },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                )

            }
            item {
                var isFieldFocused by remember { mutableStateOf(false) }
                OutlinedTextField(
                        value = newUserPhone,
                        onValueChange = { newUserPhone = it },
                        label = { Text("Phone") },
                        placeholder = { Text("Enter your phone number") },
                        singleLine = true,
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp)
                                .onFocusChanged { isFieldFocused = it.isFocused },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                )
            }
            item {
                var isFieldFocused by remember { mutableStateOf(false) }
                OutlinedTextField(
                        value = newUserEmail,
                        onValueChange = { newUserEmail = it },
                        label = { Text("Email") },
                        placeholder = { Text("Enter your email") },
                        singleLine = true,
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp)
                                .onFocusChanged { isFieldFocused = it.isFocused },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                )
            }
            item {
                var isFieldFocused by remember { mutableStateOf(false) }
                OutlinedTextField(
                        value = newUserPassword,
                        onValueChange = { newUserPassword = it },
                        label = { Text("Password") },
                        placeholder = { Text("Enter your password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp)
                                .onFocusChanged { isFieldFocused = it.isFocused },
                        keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done),

                )
            }
            item {
                var isFieldFocused by remember { mutableStateOf(false) }
                OutlinedTextField(
                        value = newUserPasswordConfirm,
                        onValueChange = { newUserPasswordConfirm = it },
                        label = { Text("Password confirm") },
                        placeholder = { Text("Confirm your password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp)
                                .onFocusChanged { isFieldFocused = it.isFocused },
                        keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done),
                )
            }
            item {
                Row (modifier = Modifier.padding(top = 50.dp)) {
                    Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = {
                                checkedState.value = it
                                isChecked = !isChecked
                            },
                            colors = CheckboxDefaults.colors(Color(android.graphics.Color.parseColor("#0560FA")))

                    )
                    Text(
                            modifier = Modifier.padding(start = 2.dp, top = 13.dp),
                            text = "Agree with our policy",
                            fontSize = 16.sp
                    )
                }
            }
            item {
                Button(
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 50.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                                Color(android.graphics.Color.parseColor("#0560FA"))
                        ),
                        onClick = {
                            if (checkPhone(newUserPhone) && checkEmail(newUserEmail) && checkPasswordConfirm(newUserPassword, newUserPasswordConfirm)) {
                                composableScope.launch(Dispatchers.IO) {
                                    if (isChecked) {
                                        try {
                                            if (signUp(newUserEmail, newUserPassword)) {
                                                if (getIdUser(newUserEmail).isNotEmpty()) {
                                                    supabase.from("users_public").insert(
                                                            listOf(
                                                                    mapOf(
                                                                            "id_user" to getIdUser(newUserEmail),
                                                                            "fullName" to newUserFullName,
                                                                            "phone" to newUserPhone,
                                                                            "email" to newUserEmail,
                                                                            "rider" to false
                                                                    )
                                                            )
                                                    ) {
                                                        select()
                                                        single()
                                                    }

                                                    /*val userPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
                                                    userPref.edit().putString("userName", newUserFullName).apply()
                                                    userPref.edit().putString("userPhone", newUserPhone).apply()
                                                    userPref.edit().putString("userEmail", newUserEmail).apply()
                                                    userPref.edit().putString("userPassword", newUserPassword).apply()*/

                                                    getSharedPreferences("firstUserData", Context.MODE_PRIVATE)
                                                            .edit().putBoolean("isUserFirstTime", false).apply()

                                                    getSharedPreferences("firstUserData", Context.MODE_PRIVATE)
                                                            .edit().putBoolean("isUserSignUp", true).apply()

                                                    withContext(Dispatchers.Main) {
                                                        startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
                                                        finish()
                                                    }

                                                    newUserFullName = ""
                                                    newUserPhone = ""
                                                    newUserEmail = ""
                                                    newUserPassword = ""
                                                    newUserPasswordConfirm = ""

                                                } else {
                                                    withContext(Dispatchers.Main) {
                                                        Toast.makeText(
                                                                this@RegistrationActivity,
                                                                "idUser",
                                                                Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            } else {
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(
                                                            this@RegistrationActivity,
                                                            "SignUp",
                                                            Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        } catch (e: Exception) {
                                            Log.e("General issue","Error: ${e.message}",e)
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(
                                                        this@RegistrationActivity,
                                                        "General Error",
                                                        Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(
                                                    this@RegistrationActivity,
                                                    "CheckBox",
                                                    Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } //scope
                            } else {
                                Toast.makeText(
                                        this@RegistrationActivity,
                                        "Check input data",
                                        Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                ) {
                    Text(text = "Sign Up")
                }
            }
            item {
                Row (
                        modifier = Modifier
                                .padding(top = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                            text = "Already have an account?",
                            color = Color(android.graphics.Color.parseColor("#A7A7A7"))
                    )
                    Text(
                            modifier = Modifier
                                    .clickable {
                                        startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
                                        finish()
                                    }
                                    .padding(start = 5.dp),
                            text = "Sign In",
                            color = Color(android.graphics.Color.parseColor("#0560FA"))
                    )
                }
            }
        }
    }

    private fun checkPhone(phone: String): Boolean {
        val regex = Regex("""^\+\d{11}$""")
        return regex.matches(phone)
    }

    private fun checkEmail(email: String): Boolean {
        val regex = Regex("""^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$""")
        return regex.matches(email)
    }

    private fun checkPasswordConfirm(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    @Preview(showBackground = true)
    @Composable
    fun RegistrationPreview() {
        Registration()
    }
}