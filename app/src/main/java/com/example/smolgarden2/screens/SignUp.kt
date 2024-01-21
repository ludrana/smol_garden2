package com.example.smolgarden2.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smolgarden2.Routes
import com.example.smolgarden2.writeUserData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun SignupForm(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isPassword2Valid by remember { mutableStateOf(true) }
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            LoginField(
                value = email,
                onChange = {
                    email = it
                    isEmailValid = isValidEmail(email)
                },
                modifier = Modifier.fillMaxWidth(),
                isValid = isEmailValid
            )
            PasswordField(
                value = password,
                onChange = {
                    password = it
                    isPasswordValid = password.length >= 6
                },
                submit = { },
                modifier = Modifier.fillMaxWidth(),
                isValid = isPasswordValid
            )
            PasswordField(
                value = password2,
                label = "Repeat password",
                onChange = {
                    password2 = it
                    isPassword2Valid = password == password2
                           },
                submit = { },
                modifier = Modifier.fillMaxWidth(),
                isValid = isPassword2Valid,
                invalidWarning = "Passwords must match"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (isEmailValid and isPasswordValid and isPassword2Valid) {
                        performSignUp(email, password, navController)
                    }
                },
                enabled = true,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }
    }
}

fun performSignUp(email: String, password: String, navController: NavController) {
    Firebase.auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("auth", "createUserWithEmail:success")
                Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("auth", "signInWithEmail:success")
                            navController.navigate(Routes.Home.route) { popUpTo(Routes.Settings.route) { inclusive = true } }
                            writeUserData()
                        } else {
                            Log.w("auth", "signInWithEmail:failure", task.exception)
                        }
                    }
            } else {
                Log.w("auth", "createUserWithEmail:failure", task.exception)
            }
        }
}
