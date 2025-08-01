package com.example.nfcreader.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nfcreader.data.db.TagEntity

// Composable affichant l'historique des tags NFC lus sous forme de liste verticale
@Composable
fun HistoryScreen(history: List<TagEntity>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()   // Prend toute la taille disponible de l'écran
            .padding(16.dp), // Padding autour de la liste
        verticalArrangement = Arrangement.spacedBy(8.dp) // Espacement entre les éléments
    ) {
        // Pour chaque élément dans l'historique, on crée un item de liste
        items(history) { tag ->
            Card(modifier = Modifier.fillMaxWidth()) { // Card pour style avec ombre et coins arrondis
                Column(modifier = Modifier.padding(12.dp)) { // Padding intérieur de la Card
                    // Affiche l'ID du tag avec un emoji, en style titre moyen
                    Text("🆔 ${tag.tagId}", style = MaterialTheme.typography.titleMedium)
                    // Affiche le contenu lu sur le tag
                    Text("📄 ${tag.content}")
                    // Affiche la date et l'heure de la lecture, formatée en jj/MM/aaaa HH:mm
                    Text(
                        "⏱ ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(tag.scannedAt)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
