package com.example.darkapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown

// Theme with light/dark mode switch (for better UX)
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = Typography(),
        content = content
    )
}

// Load builds from Firestore with loading and error handling
fun loadBuilds(db: FirebaseFirestore, onSuccess: (List<Map<String, String>>) -> Unit, onFailure: () -> Unit) {
    db.collection("builds")
        .get()
        .addOnSuccessListener { result ->
            val builds = result.map { document ->
                document.data.mapValues { it.value.toString() } + mapOf("id" to document.id)
            }
            onSuccess(builds)
        }
        .addOnFailureListener { onFailure() }
}

// Delete a specific build with success/error handling
fun deleteBuild(buildId: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
    Firebase.firestore.collection("builds").document(buildId)
        .delete()
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure() }
}

// Update a specific build in Firestore
fun updateBuild(buildId: String, updatedData: Map<String, String>, onSuccess: () -> Unit, onFailure: () -> Unit) {
    Firebase.firestore.collection("builds").document(buildId)
        .set(updatedData)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure() }
}

// CharacterScreen with build list, loading, delete, and edit functionality
@Composable
fun CharacterScreen() {
    val db = Firebase.firestore
    var isLoading by remember { mutableStateOf(true) }
    var characters by remember { mutableStateOf<List<Map<String, String>>>(emptyList()) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedBuildId by remember { mutableStateOf<String?>(null) }
    var buildToEdit by remember { mutableStateOf<Map<String, String>?>(null) }

    // Load builds from Firestore
    LaunchedEffect(Unit) {
        loadBuilds(db, onSuccess = { builds ->
            characters = builds
            isLoading = false
        }, onFailure = {
            isLoading = false
        })
    }

    // Display loading spinner or content
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Todas as Builds", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))

            if (characters.isEmpty()) {
                Text("Nenhuma build encontrada.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(characters) { character ->
                        BuildCard(
                            character = character,
                            onDeleteClick = {
                                selectedBuildId = character["id"]
                                showDeleteConfirmation = true
                            },
                            onEditClick = {
                                selectedBuildId = character["id"]
                                buildToEdit = character
                                showEditDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteConfirmation) {
        DeleteConfirmationDialog(
            onConfirm = {
                selectedBuildId?.let { id ->
                    deleteBuild(id, onSuccess = {
                        characters = characters.filterNot { it["id"] == id }
                        showDeleteConfirmation = false
                    }, onFailure = {
                        // Show error message
                    })
                }
            },
            onCancel = {
                showDeleteConfirmation = false
            }
        )
    }

    // Edit build dialog
    if (showEditDialog && buildToEdit != null) {
        EditBuildDialog(build = buildToEdit!!, onSave = { updatedBuild ->
            selectedBuildId?.let { id ->
                updateBuild(id, updatedBuild, onSuccess = {
                    characters = characters.map { character ->
                        if (character["id"] == id) {
                            character + updatedBuild
                        } else {
                            character
                        }
                    }
                    showEditDialog = false
                }, onFailure = {
                    // Show error message
                })
            }
        }, onCancel = {
            showEditDialog = false
        })
    }
}

// Confirmation dialog for deletion
@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Confirmar Exclusão") },
        text = { Text("Você tem certeza que deseja excluir essa build?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sim")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    )
}

// Dialog to edit build details
@Composable
fun EditBuildDialog(build: Map<String, String>, onSave: (Map<String, String>) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf(build["nome"] ?: "") }
    var details by remember { mutableStateOf(build.filter { it.key != "nome" }) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Editar Build") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome da Build") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                details.forEach { (key, value) ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = { newValue -> details = details + (key to newValue) },
                        label = { Text(key) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val updatedBuild = mapOf("nome" to name) + details
                onSave(updatedBuild)
            }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    )
}

// Build card with collapsible section for details
@Composable
fun BuildCard(character: Map<String, String>, onDeleteClick: () -> Unit, onEditClick: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    val buildName = character["nome"] ?: "Nome da Build"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Build name with toggleable expansion
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buildName,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f).clickable {
                        isExpanded = !isExpanded // Toggle expansion on name click
                    },
                    color = MaterialTheme.colorScheme.primary
                )

                // Icon for expansion (triangle)
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                        contentDescription = "Expandir/Fechar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Animated content visibility for details
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {
                    character.forEach { (key, value) ->
                        if (key != "id" && key != "nome") {
                            Text("$key: $value", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            // Buttons for Edit and Delete
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    shape = RoundedCornerShape(12.dp) // Botão com bordas arredondadas
                ) {
                    Text("Excluir Build", color = Color.White)
                }

                Button(
                    onClick = onEditClick,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    shape = RoundedCornerShape(12.dp), // Botão com bordas arredondadas
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Cor vibrante para Editar
                ) {
                    Text("Editar Build", color = Color.White)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCharacterScreen() {
    AppTheme {
        CharacterScreen()
    }
}
