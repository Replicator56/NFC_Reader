package com.example.nfcreader.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// Représente une entité/table dans la base de données Room appelée "nfc_tags"
@Entity(tableName = "nfc_tags")
data class TagEntity(
    // Clé primaire auto-générée, identifiant unique pour chaque entrée
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // Identifiant du tag NFC (ex : l’UID du tag sous forme de chaîne hexadécimale)
    val tagId: String,

    // Contenu lu depuis le tag NFC (par exemple texte NDEF)
    val content: String,

    // Timestamp de la date/heure du scan (millisecondes depuis epoch)
    // Par défaut, la date actuelle est utilisée lors de la création de l'objet
    val scannedAt: Long = Date().time
)
