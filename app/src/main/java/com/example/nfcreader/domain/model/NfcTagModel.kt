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

// ViewModel Android qui gère la logique métier liée à la lecture des tags NFC
// Hérite d'AndroidViewModel pour avoir accès au contexte de l'application
class NfcTagViewModel(application: Application) : AndroidViewModel(application) {

    // Repository pour accéder aux données (tags sauvegardés en base)
    private val repository: TagRepository =
        TagRepository(AppDatabase.getDatabase(application).tagDao())

    // Flux des tags lus dans l'historique, observable par l'UI
    val history: StateFlow<List<TagEntity>> = repository.getHistory().stateIn(
        viewModelScope,               // scope lié au cycle de vie du ViewModel
        SharingStarted.Lazily,        // démarre la collecte seulement si quelqu'un observe
        emptyList()                   // valeur initiale vide
    )

    // Tag NFC actuellement détecté (mutable en interne, exposé en lecture seule)
    private val _currentTag = MutableStateFlow<Tag?>(null)
    val currentTag: StateFlow<Tag?> get() = _currentTag

    // ID du tag détecté (ex: UID hexadécimal)
    private val _tagId = MutableStateFlow("")
    val tagId: StateFlow<String> get() = _tagId

    // Contenu NDEF du tag détecté (texte extrait des enregistrements)
    private val _ndefContent = MutableStateFlow("")
    val ndefContent: StateFlow<String> get() = _ndefContent

    // Manager NFC qui réalise la lecture/écriture physique du tag
    private val nfcManager = NfcManager(application.applicationContext)

    // Méthode appelée à chaque détection d'un tag NFC
    fun onTagDetected(tag: Tag) {
        viewModelScope.launch {
            try {
                // Convertit l'ID du tag en chaîne hexadécimale séparée par ':'
                val id = tag.id?.joinToString(":") {
                    "%02X".format(it)
                } ?: "Inconnu"

                // Lit le contenu NDEF du tag, ou indique "Tag vide" si absent
                val content = nfcManager.readTag(tag) ?: "Tag vide"

                // Mise à jour des StateFlow observés par l'UI
                _currentTag.value = tag
                _tagId.value = id
                _ndefContent.value = content

                // Sauvegarde dans la base de données
                repository.saveTag(id, content)
            } catch (e: Exception) {
                // En cas d'erreur, met à jour le contenu affiché avec un message d'erreur
                _ndefContent.value = "Erreur lecture NFC"
            }
        }
    }
}
