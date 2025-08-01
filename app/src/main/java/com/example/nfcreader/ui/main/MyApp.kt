package com.example.nfcreader.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nfcreader.vm.NfcViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(viewModel: NfcViewModel) {
    // Collecte les flows StateFlow du ViewModel en état Compose avec cycle de vie
    val tagId by viewModel.tagId.collectAsStateWithLifecycle()
    val ndefText by viewModel.ndefContent.collectAsStateWithLifecycle()
    val currentTag by viewModel.currentTag.collectAsStateWithLifecycle()

    // État pour afficher des Snackbar (notifications temporaires)
    val snackbarHostState = remember { SnackbarHostState() }
    // État local pour stocker le texte à écrire sur le tag
    var messageToWrite by remember { mutableStateOf("") }
    // CoroutineScope pour lancer des coroutines (notamment pour la Snackbar)
    val coroutineScope = rememberCoroutineScope()

    // Scaffold fournit une structure standard avec support des snackbars
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize() // prend tout l’espace disponible
                .padding(padding) // padding fourni par Scaffold (systèmes UI)
                .padding(16.dp), // padding interne
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // espace entre les éléments
        ) {
            Text("📡 NFC Compose Reader/Writer", style = MaterialTheme.typography.headlineMedium)

            // Affiche l’ID du tag détecté (vide si aucun)
            Text("🆔 Tag ID : $tagId")

            // Affiche le contenu NDEF lu sur le tag
            Text("📄 Contenu NDEF : $ndefText")

            // Champ texte pour saisir le texte à écrire sur le tag
            OutlinedTextField(
                value = messageToWrite,
                onValueChange = { messageToWrite = it },
                label = { Text("Texte à écrire sur le tag") },
                modifier = Modifier.fillMaxWidth()
            )

            // Bouton d’écriture : actif seulement si un tag est détecté
            Button(
                onClick = {
                    // Appelle la méthode d’écriture dans le ViewModel
                    viewModel.writeText(messageToWrite) { success ->
                        val msg = if (success) "✅ Écriture réussie" else "❌ Échec écriture"
                        coroutineScope.launch {
                            // Affiche la snackbar selon succès ou échec
                            snackbarHostState.showSnackbar(msg)
                        }
                    }
                    // Vide le champ texte après l’écriture
                    messageToWrite = ""
                },
                enabled = currentTag != null
            ) {
                Text("✍️ Écrire sur le Tag")
            }
        }
    }
}
