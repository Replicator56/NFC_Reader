package com.example.nfcreader.data.repository

import com.example.nfcreader.data.db.TagDao
import com.example.nfcreader.data.db.TagEntity
import kotlinx.coroutines.flow.Flow

class TagRepository(private val dao: TagDao) {
    fun getHistory(): Flow<List<TagEntity>> = dao.getAllTags()
    suspend fun saveTag(tagId: String, content: String) {
        dao.insert(TagEntity(tagId = tagId, content = content))
    }
}
