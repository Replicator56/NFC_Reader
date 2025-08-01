package com.example.nfcreader.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Interface DAO (Data Access Object) pour accéder à la table NFC tags
@Dao
interface TagDao {

    // Insère un tag dans la base de données
    // En cas de conflit (ex: clé primaire déjà existante), l'insertion est ignorée
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tag: TagEntity)

    // Requête pour récupérer tous les tags, triés par date de scan décroissante
    // Renvoie un Flow qui émet automatiquement les mises à jour de la liste
    @Query("SELECT * FROM nfc_tags ORDER BY scannedAt DESC")
    fun getAllTags(): Flow<List<TagEntity>>
}
