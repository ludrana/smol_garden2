package com.example.smolgarden2.screens

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smolgarden2.Routes
import com.example.smolgarden2.readUserData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun LoginForm(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf(true) }
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
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                          if (isEmailValid and isPasswordValid) {
                              Firebase.auth.signInWithEmailAndPassword(email, password)
                                  .addOnCompleteListener { task ->
                                      if (task.isSuccessful) {
                                          Log.d("auth", "signInWithEmail:success")
                                          readUserData()
                                          navController.navigate(Routes.Home.route) { popUpTo(Routes.Home.route) { inclusive = true } }
                                      } else {
                                          Log.w("auth", "signInWithEmail:failure", task.exception)
                                      }
                                  }
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

fun isValidEmail(target: CharSequence?): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

@Composable
fun LoginField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Email",
    placeholder: String = "Enter your email",
    isValid: Boolean = true,
    invalidWarning: String = "Enter valid email"
) {

    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Person,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )

    if (!isValid) {
        Text(text = invalidWarning, color = Color.Red, modifier = Modifier.padding(5.dp))
    }
}

@Composable
fun PasswordField(
    value: String,
    onChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    placeholder: String = "Enter your Password",
    isValid: Boolean = true,
    invalidWarning: String = "Password must be at least 6 characters long"
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Key,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }


    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = { submit() }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )

    if (!isValid) {
        Text(text = invalidWarning, color = Color.Red, modifier = Modifier.padding(5.dp))
    }
}