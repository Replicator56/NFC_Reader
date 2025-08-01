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

    // Repository pour gérer les accès à la base de données (DAO)
    private val repository: TagRepository =
        TagRepository(AppDatabase.getDatabase(application).tagDao())

    // Flux StateFlow exposant l'historique des tags lus, écouté par l'UI
    val history: StateFlow<List<TagEntity>> = repository.getHistory().stateIn(
        viewModelScope,
        SharingStarted.Lazily,  // le flux ne démarre qu'à la première collecte
        emptyList()             // valeur initiale vide
    )

    // État interne pour le tag NFC actuellement détecté (nullable)
    private val _currentTag = MutableStateFlow<Tag?>(null)
    val currentTag: StateFlow<Tag?> get() = _currentTag

    // État interne pour l'ID du tag (hex string)
    private val _tagId = MutableStateFlow("")
    val tagId: StateFlow<String> get() = _tagId

    // État interne pour le contenu NDEF lu
    private val _ndefContent = MutableStateFlow("")
    val ndefContent: StateFlow<String> get() = _ndefContent

    // Manager NFC, injecté depuis MainActivity (nullable car initialisé après création du ViewModel)
    var nfcManager: NfcManager? = null

    // Fonction appelée lors de la détection d'un tag NFC
    fun onTagDetected(tag: Tag) {
        viewModelScope.launch {
            try {
                // Extraction de l'ID du tag en format hexadécimal (ex: "04:A2:B3:1F")
                val id = tag.id?.joinToString(":") { "%02X".format(it) } ?: "Unknown"
                // Lecture du contenu NDEF via le manager NFC, ou message "Tag vide" si absent
                val content = nfcManager?.readTag(tag) ?: "Tag vide"

                // Mise à jour des états observés
                _currentTag.value = tag
                _tagId.value = id
                _ndefContent.value = content

                // Sauvegarde du tag lu en base de données
                repository.saveTag(id, content)
            } catch (e: Exception) {
                // En cas d'erreur de lecture, on affiche un message d'erreur
                _ndefContent.value = "Erreur lecture NFC"
            }
        }
    }

    // Fonction pour écrire un texte sur le tag NFC actuel
    fun writeText(text: String, onResult: (Boolean) -> Unit) {
        val tag = _currentTag.value ?: run {
            // Si aucun tag détecté, on appelle le callback avec false
            onResult(false)
            return
        }

        viewModelScope.launch {
            try {
                // Tentative d'écriture via le manager NFC
                val success = nfcManager?.writeTag(tag, text) ?: false
                if (success) {
                    // Si réussite, on sauvegarde le nouveau contenu en base
                    repository.saveTag(_tagId.value, text)
                    _ndefContent.value = text
                }
                // On informe le caller du résultat (succès ou échec)
                onResult(success)
            } catch (e: Exception) {
                // En cas d'erreur, on renvoie false
                onResult(false)
            }
        }
    }
}
