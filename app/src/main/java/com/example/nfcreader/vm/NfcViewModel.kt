package com.example.nfcreader.vm

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

class NfcViewModel(application: Application) : AndroidViewModel(application) {

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

    // ✅ Manager rendu nullable, injecté depuis MainActivity
    var nfcManager: NfcManager? = null

    fun onTagDetected(tag: Tag) {
        viewModelScope.launch {
            try {
                val id = tag.id?.joinToString(":") { "%02X".format(it) } ?: "Unknown"
                val content = nfcManager?.readTag(tag) ?: "Tag vide"

                _currentTag.value = tag
                _tagId.value = id
                _ndefContent.value = content

                repository.saveTag(id, content)
            } catch (e: Exception) {
                _ndefContent.value = "Erreur lecture NFC"
            }
        }
    }

    fun writeText(text: String, onResult: (Boolean) -> Unit) {
        val tag = _currentTag.value ?: run {
            onResult(false)
            return
        }

        viewModelScope.launch {
            try {
                val success = nfcManager?.writeTag(tag, text) ?: false
                if (success) {
                    repository.saveTag(_tagId.value, text)
                    _ndefContent.value = text
                }
                onResult(success)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}
