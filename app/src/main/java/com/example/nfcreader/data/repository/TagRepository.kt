package com.example.nfcreader.data.repository

import com.example.nfcreader.data.db.TagDao
import com.example.nfcreader.data.db.TagEntity
import kotlinx.coroutines.flow.Flow

// Repository pour accéder aux données des tags NFC.
// Abstraction entre la source de données (DAO) et la couche supérieure (ViewModel).
class TagRepository(private val dao: TagDao) {

    // Récupère l’historique des tags lus sous forme de Flow (stream asynchrone).
    fun getHistory(): Flow<List<TagEntity>> = dao.getAllTags()

    // Sauvegarde un nouveau tag dans la base de données.
    // Crée un TagEntity avec l’ID et le contenu puis l’insère via le DAO.
    suspend fun saveTag(tagId: String, content: String) {
        dao.insert(TagEntity(tagId = tagId, content = content))
    }
}
