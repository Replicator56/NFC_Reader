package com.example.nfcreader.domain.model

import android.app.Application
import android.nfc.Tag
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfcreader.data.db.AppDatabase
import com.example.nfcreader.data.db.TagEntity
import com.example.nfcreader.data.repository.TagRepository
import com.example.nfcreader.nfc.NfcManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NfcTagViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TagRepository =
        TagRepository(AppDatabase.getDatabase(application).tagDao())

    val history: StateFlow<List<TagEntity>> = repository.getHistory().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    private val _currentTag = MutableStateFlow<Tag?>(null)
    val currentTag: StateFlow<Tag?> get() = _currentTag

    private val _tagId = MutableStateFlow("")
    val tagId: StateFlow<String> get() = _tagId

    private val _ndefContent = MutableStateFlow("")
    val ndefContent: StateFlow<String> get() = _ndefContent

    private val nfcManager = NfcManager(application.applicationContext)

    fun onTagDetected(tag: Tag) {
        viewModelScope.launch {
            try {
                val id = tag.id?.joinToString(":") {
                    "%02X".format(it)
                } ?: "Inconnu"
                val content = nfcManager.readTag(tag) ?: "Tag vide"
                _currentTag.value = tag
                _tagId.value = id
                _ndefContent.value = content

                repository.saveTag(id, content)
            } catch (e: Exception) {
                _ndefContent.value = "Erreur lecture NFC"
            }
        }
    }
}
