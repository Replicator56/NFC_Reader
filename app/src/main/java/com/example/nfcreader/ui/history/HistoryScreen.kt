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

@Composable
fun HistoryScreen(history: List<TagEntity>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(history) { tag ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("🆔 ${tag.tagId}", style = MaterialTheme.typography.titleMedium)
                    Text("📄 ${tag.content}")
                    Text("⏱ ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(tag.scannedAt)}",
                        style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
