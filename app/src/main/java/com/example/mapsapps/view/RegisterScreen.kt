package com.example.mapsapps.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.mapsapps.R
import com.example.mapsapps.navigations.Routes
import com.example.mapsapps.viewModel.MapsViewModel

@Composable
fun RegisterScreen(navController: NavController, mapsViewModel: MapsViewModel) {
    val returnToLogIn by mapsViewModel.returnToLogIn.observeAsState(false)
    val email by mapsViewModel.email.observeAsState("")
    val password by mapsViewModel.password.observeAsState("")
    val passwordCheck by mapsViewModel.passwordCheck.observeAsState("")
    var passwordVisivility by remember { mutableStateOf(false) }
    FailRegisterAlert(mapsViewModel)
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(0.9f)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Color(0xFF90e0ef)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Creación de cuenta",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color(0xFF03045e)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(painter = painterResource(id = R.drawable.ic_login),
                contentDescription = "icono",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { mapsViewModel.setMail(it) },
                label = {
                    Text(
                        "Correo electrónico",
                        color = Color(0xFF03045e),
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF03045e),
                    unfocusedBorderColor = Color(0xFF03045e),
                    cursorColor = Color(0xFF03045e),
                    focusedTextColor = Color(0xFF03045e),
                    unfocusedTextColor = Color(0xFF03045e),

                    ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )

            )
            OutlinedTextField(
                value = password,
                onValueChange = { mapsViewModel.setPassword(it) },
                label = {
                    Text(
                        "Contraseña",
                        color = Color(0xFF03045e)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF03045e),
                    unfocusedBorderColor = Color(0xFF03045e),
                    cursorColor = Color(0xFF03045e),
                    focusedTextColor = Color(0xFF03045e),
                    unfocusedTextColor = Color(0xFF03045e),

                    ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if (passwordVisivility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisivility = !passwordVisivility }) {
                        Icon(
                            imageVector = if (passwordVisivility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "visibility",
                            tint = Color(0xFF03045e)
                        )
                    }

                }


            )
            OutlinedTextField(
                value = passwordCheck,
                onValueChange = { mapsViewModel.setPasswordCheck(it) },
                label = {
                    Text(
                        "Repetir Contraseña",
                        color = Color(0xFF03045e)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF03045e),
                    unfocusedBorderColor = Color(0xFF03045e),
                    cursorColor = Color(0xFF03045e),
                    focusedTextColor = Color(0xFF03045e),
                    unfocusedTextColor = Color(0xFF03045e),

                    ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if (passwordVisivility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisivility = !passwordVisivility }) {
                        Icon(
                            imageVector = if (passwordVisivility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "visibility",
                            tint = Color(0xFF03045e)
                        )
                    }

                }


            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp)
                    .fillMaxWidth(0.8f),
                onClick = {
                    try {
                        if (password == passwordCheck && password.isNotEmpty() && email.isNotEmpty() && password.length >= 6 && email.contains(
                                "@"
                            ) && email.contains(".")
                        ) {
                            mapsViewModel.registerUser(email, password)
                            mapsViewModel.setReturnToLogIn(true)
                            if (returnToLogIn) {
                                navController.navigate(Routes.Login.route)
                                mapsViewModel.email.value = ""
                                mapsViewModel.password.value = ""
                            }
                        } else {
                            mapsViewModel.setReturnToLogIn(false)
                            mapsViewModel.setOpenerDialog(true)
                        }
                    } catch (e: Exception) {
                        mapsViewModel.setReturnToLogIn(false)
                        mapsViewModel.setOpenerDialog(true)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFcaf0f8), contentColor = Color(0xFF03045e)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Crear cuenta")
            }
            Button(
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp),
                onClick = { navController.navigate(Routes.Login.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFcaf0f8), contentColor = Color(0xFF03045e)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Volver")
            }

        }
    }
}

@Composable
fun FailRegisterAlert(mapsViewModel: MapsViewModel) {
    val dialogOpener by mapsViewModel.dialogOpener.observeAsState(false)
    if (dialogOpener) {
        Dialog(onDismissRequest = { mapsViewModel.setOpenerDialog(false) }) {
            Surface(
                color = Color(0xFF90e0ef),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Error al crear la cuenta, intente de nuevo.",
                        textAlign = TextAlign.Center,
                        color = Color(0xFF03045e)
                    )
                }
            }
        }
    }
}


