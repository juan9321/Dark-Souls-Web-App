package com.example.darkapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)), // Fundo escuro
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .background(Color(0xFF292929), shape = RoundedCornerShape(16.dp)) // Fundo do formulário
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Bem-vindo ao DarkApp",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF4D03F), // Dourado
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Campo de email
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF333333),
                    focusedIndicatorColor = Color(0xFFF4D03F), // Indicador dourado
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFFF4D03F),
                    focusedTextColor = Color.White, // Texto digitado (focado) branco
                    unfocusedTextColor = Color.White // Texto digitado (não focado) branco
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de senha
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF333333),
                    focusedIndicatorColor = Color(0xFFF4D03F),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFFF4D03F),
                    focusedTextColor = Color.White, // Texto digitado (focado) branco
                    unfocusedTextColor = Color.White // Texto digitado (não focado) branco
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Botão de login
            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onLoginSuccess()
                            } else {
                                errorMessage = task.exception?.message ?: "Falha no login"
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF4D03F), // Cor do botão dourado
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp) // Bordas arredondadas
            ) {
                Text(
                    text = "Entrar",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Mensagem de erro
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFE74C3C), // Vermelho
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthScreen(onLoginSuccess = {})
}
