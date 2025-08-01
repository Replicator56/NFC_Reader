package com.example.nfcreader.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "nfc_tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tagId: String,
    val content: String,
    val scannedAt: Long = Date().time
)
