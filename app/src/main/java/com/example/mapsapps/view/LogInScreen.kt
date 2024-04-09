package com.example.mapsapps.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.mapsapps.R
import com.example.mapsapps.data.UserPrefs
import com.example.mapsapps.navigations.Routes
import com.example.mapsapps.viewModel.MapsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LogInScreen(navController: NavController, mapsViewModel: MapsViewModel) {
    val context = LocalContext.current
    val userPrefs = UserPrefs(context)
    val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())
    val email by mapsViewModel.email.observeAsState("")
    val password by mapsViewModel.password.observeAsState("")
    var passwordVisivility by remember { mutableStateOf(false) }
    val areWeLoggedIn by mapsViewModel.areWeLoggedIn.observeAsState(false)
    val saveData by mapsViewModel.saveData.observeAsState(false)


    if (storedUserData.value.isNotEmpty() && storedUserData.value[0] != ""
        && storedUserData.value[1] != "" && mapsViewModel.areWeLoggedInAndRemembered.value == true
    ) {
        mapsViewModel.setMail(storedUserData.value[0])
        mapsViewModel.setPassword(storedUserData.value[1])
        mapsViewModel.loginUser(storedUserData.value[0], storedUserData.value[1])
    }
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
            Image(
                painter = painterResource(id = R.drawable.ic_login),
                contentDescription = "icono",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Text(
                text = "Bienvenido a MapsApp! \nPor favor, inicie sesión para continuar.",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color(0xFF03045e)
            )
            Spacer(modifier = Modifier.height(16.dp))
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

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recúerdame",
                    color = Color(0xFF03045e),
                    textDecoration = TextDecoration.Underline
                )
                Checkbox(
                    checked = saveData,
                    onCheckedChange = { mapsViewModel.setSaveData(it) },
                    colors = CheckboxDefaults.colors(
                        checkmarkColor = Color(0xFF90e0ef),
                        checkedColor = Color(0xFF03045e),
                        uncheckedColor = Color(0xFF03045e)
                    )
                )
            }

            Button(
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp)
                    .fillMaxWidth(0.8f),
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty() && email.contains("@") && email.contains(
                            "."
                        ) && password.length >= 6
                    ) {
                        mapsViewModel.loginUser(email, password)
                        if (saveData) {
                            CoroutineScope(Dispatchers.IO).launch {
                                userPrefs.saveUserData(email, password)
                            }
                        }
                    } else {
                        mapsViewModel.setOpenerDialog(true)
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFcaf0f8), contentColor = Color(0xFF03045e)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Iniciar sesión")
            }
            LaunchedEffect(areWeLoggedIn) {
                if (areWeLoggedIn == true) {
                    navController.navigate(Routes.Map.route)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.padding(bottom = 16.dp)) {
                Text(
                    "¿No tienes una cuenta? ",
                    color = Color(0xFF03045e)
                )
                Text(
                    text = "Regístrate",
                    color = Color(0xFF03045e),
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .clickable { navController.navigate(Routes.Register.route) })
            }
        }
    }
    FailLogInAlert(mapsViewModel)

}


@Composable
fun FailLogInAlert(mapsViewModel: MapsViewModel) {
    val dialogOpener by mapsViewModel.dialogOpener.observeAsState(false)
    if (dialogOpener) {
        Dialog(onDismissRequest = { mapsViewModel.setOpenerDialog(false) }) {
            Surface(
                color = Color(0xFF90e0ef),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Error al iniciar sesión, intente de nuevo.",
                        textAlign = TextAlign.Center,
                        color = Color(0xFF03045e)
                    )
                }
            }
        }
    }
}

