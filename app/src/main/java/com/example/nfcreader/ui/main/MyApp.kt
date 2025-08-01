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
    val tagId by viewModel.tagId.collectAsStateWithLifecycle()
    val ndefText by viewModel.ndefContent.collectAsStateWithLifecycle()
    val currentTag by viewModel.currentTag.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var messageToWrite by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("📡 NFC Compose Reader/Writer", style = MaterialTheme.typography.headlineMedium)
            Text("🆔 Tag ID : $tagId")
            Text("📄 Contenu NDEF : $ndefText")

            OutlinedTextField(
                value = messageToWrite,
                onValueChange = { messageToWrite = it },
                label = { Text("Texte à écrire sur le tag") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.writeText(messageToWrite) { success ->
                        val msg = if (success) "✅ Écriture réussie" else "❌ Échec écriture"
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(msg)
                        }
                    }
                    messageToWrite = ""
                },
                enabled = currentTag != null
            ) {
                Text("✍️ Écrire sur le Tag")
            }
        }
    }
}
