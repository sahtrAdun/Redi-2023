package com.example.kotik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OtpActivity : ComponentActivity() {

    private var otpCode = 0
    private var userEmail by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OTP_first()
        }
    }

    @Composable
    fun OTP_first() {
        val composableScope = rememberCoroutineScope()
        LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(start = 25.dp, end = 25.dp)) {
            item {
                Column(modifier = Modifier.padding(top = 100.dp)) {
                    Text(
                            text = "Forgot Password",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                    )
                    Text(
                            text = "Enter your email address",
                            fontSize = 14.sp,
                            color = Color(android.graphics.Color.parseColor("#A7A7A7"))
                    )
                }
            }
            item {
                var isEmailFocused by remember { mutableStateOf(false) }
                OutlinedTextField(
                        value = userEmail,
                        onValueChange = { userEmail = it },
                        label = { Text("Email") },
                        placeholder = { Text("Enter your email") },
                        singleLine = true,
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 60.dp)
                                .onFocusChanged { isEmailFocused = it.isFocused },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                )
            }
            item {
                otpCode = generateSixDigitRandomNumber()
                Button(
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 100.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                                Color(android.graphics.Color.parseColor("#0560FA"))
                        ),onClick = {
                            composableScope.launch {
                                if (checkEmailExists(userEmail)) {
                                    setContent {
                                        OTP_second()
                                }
                            }
                        }

                }
                )
                {
                    Text(
                            text = "Send OTP"
                    )
                }
            }
            item {
                Row (
                        modifier = Modifier
                                .padding(top = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                            text = "Remember password? Back to ",
                            color = Color(android.graphics.Color.parseColor("#A7A7A7"))
                    )
                    Text(
                            modifier = Modifier
                                    .clickable {
                                        startActivity(Intent(this@OtpActivity, LoginActivity::class.java))
                                        finish()
                                    }
                                    .padding(start = 5.dp),
                            text = "Log In",
                            color = Color(android.graphics.Color.parseColor("#0560FA"))
                    )
                }
            }
        }
    }

    @Composable
    fun OTP_second() {
        val code = remember { mutableStateListOf("", "", "", "", "", "") }
        val fullCode = remember { mutableStateListOf("") }
        val focusRequester = remember { FocusRequester() }
        val focusRequesterList = remember {
            List(6) { FocusRequester() }
        }

        var isCorrect by remember {mutableStateOf(false) }

        LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(start = 25.dp, end = 25.dp)) {
            item {
                Column(modifier = Modifier.padding(top = 100.dp)) {
                    Text(
                            text = "OTP Verification",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                    )
                    Text(
                            text = "Enter the 6 digit numbers sent to your email",
                            fontSize = 14.sp,
                            color = Color(android.graphics.Color.parseColor("#A7A7A7"))
                    )
                }
            }
            item {
                Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 75.dp)
                ) {
                    for (i in 0 until 6) {
                        if (i < code.size) { // Проверяем, существует ли элемент в массиве code
                            OutlinedTextField(
                                    value = code[i],
                                    onValueChange = {
                                        if (it.length <= 1) {
                                            code[i] = it
                                            if (it.isNotEmpty() && i < 5) {
                                                focusRequesterList[i + 1].requestFocus()
                                            }
                                        }
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                            .weight(1f)
                                            .padding(4.dp)
                                            .height(56.dp)
                                            .focusRequester(focusRequesterList[i]),
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = if (i == 5) ImeAction.Done else ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                            onNext = {
                                                //
                                            },
                                            onDone = {
                                                val enteredCode = code.joinToString("") // Преобразуем массив в строку
                                                if (enteredCode == otpCode.toString()) {
                                                    isCorrect = true
                                                }
                                            }
                                    ),
                                    textStyle = TextStyle.Default.copy(
                                            fontSize = 24.sp,
                                            textAlign = TextAlign.Center
                                    ),
                                    colors = TextFieldDefaults.colors(
                                            cursorColor = Color.Transparent
                                    )
                            )
                        }
                    }
                }

            }
            item {
                Row(
                        modifier = Modifier.padding(top = 15.dp),
                        horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "If you did’t receive code, ")
                    Text(
                            modifier = Modifier
                                    .clickable {

                                    }
                                    .padding(start = 5.dp),
                            text = "resend",
                            color = Color(android.graphics.Color.parseColor("#0560FA"))
                    )
                }
            }
            item {
                Button(
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 100.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                                Color(android.graphics.Color.parseColor("#0560FA"))
                        ),onClick = {
                            if (isCorrect) {
                                setContent{OTP_third()}
                            }
                    }
                )
                {
                    Text(
                            text = "Set New Password"
                    )
                }
            }
            item {
                Row (
                        modifier = Modifier
                                .padding(top = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                            text = "Remember password? Back to ",
                            color = Color(android.graphics.Color.parseColor("#A7A7A7"))
                    )
                    Text(
                            modifier = Modifier
                                    .clickable {
                                        startActivity(Intent(this@OtpActivity, LoginActivity::class.java))
                                        finish()
                                    }
                                    .padding(start = 5.dp),
                            text = "Log In",
                            color = Color(android.graphics.Color.parseColor("#0560FA"))
                    )
                }
            }
            item {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = "Debug: $otpCode")
                }
            }
        }
    }

    @Composable
    fun OTP_third() {
        var newUserPassword by remember { mutableStateOf("") }
        var newUserPasswordConfirm by remember { mutableStateOf("") }
        val composableScope = rememberCoroutineScope()

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.padding(start = 25.dp, end = 25.dp)) {
                item {
                    Column(
                            modifier = Modifier
                                    .padding(top = 100.dp),
                    ) {
                        Text(
                                text = "New Password",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                        )
                        Text(
                                text = "Enter new password",
                                fontSize = 16.sp,
                                color = Color(android.graphics.Color.parseColor("#A7A7A7"))
                        )
                    }
                }
                item {
                    var isFieldFocused by remember { mutableStateOf(false) }
                    OutlinedTextField(
                            value = newUserPassword,
                            onValueChange = { newUserPassword = it },
                            label = { Text("New Password") },
                            placeholder = { Text("Enter new password") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 100.dp)
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
                    Button(
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 100.dp),
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.buttonColors(
                                    Color(android.graphics.Color.parseColor("#0560FA"))
                            ),
                            onClick = {
                                if (newUserPassword == newUserPasswordConfirm) {
                                    composableScope.launch(Dispatchers.IO) {
                                        try {
                                            updateUserPassword(userEmail)

                                            startActivity(Intent(this@OtpActivity, LoginActivity::class.java))
                                            finish()
                                        } catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(this@OtpActivity, "Update", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(this@OtpActivity, "Error", Toast.LENGTH_SHORT).show()
                                }
                            }

                    )
                    {
                        Text(
                                text = "Log In"
                        )
                    }
                }
                item {
                    Row (
                            modifier = Modifier
                                    .padding(top = 15.dp),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                                text = "Remember password? Back to ",
                                color = Color(android.graphics.Color.parseColor("#A7A7A7"))
                        )
                        Text(
                                modifier = Modifier
                                        .clickable {
                                            startActivity(Intent(this@OtpActivity, LoginActivity::class.java))
                                            finish()
                                        }
                                        .padding(start = 5.dp),
                                text = "Log In",
                                color = Color(android.graphics.Color.parseColor("#0560FA"))
                        )
                    }
                }
            }
        }
    }

    suspend fun checkEmailExists(email: String): Boolean {
        val result = supabase.from("users")
                .select {
                    filter { User::email eq email }
                }
        return result.data?.isNotEmpty() == true
    }

    private suspend fun updateUserPassword(email: String) {
        try {
            val user = supabase.auth.resetPasswordForEmail(email)
        } catch (e: Exception) {
            Log.e("UpdateUserPassword", "Error: ${e.message}", e)
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        OTP_second()
    }
    private fun generateSixDigitRandomNumber(): Int {
        return (100000..999999).random()
    }
}