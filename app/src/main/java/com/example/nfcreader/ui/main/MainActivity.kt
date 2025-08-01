package com.example.nfcreader.ui.main

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.nfcreader.nfc.NfcManager
import com.example.nfcreader.vm.NfcViewModel

class MainActivity : ComponentActivity() {

    // Obtient une instance du ViewModel associée à cette activité
    private val viewModel: NfcViewModel by viewModels()
    private lateinit var nfcManager: NfcManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialise le gestionnaire NFC avec le contexte de l'activité
        nfcManager = NfcManager(this)
        // Injecte le NfcManager dans le ViewModel pour lecture/écriture des tags
        viewModel.nfcManager = nfcManager

        // Définit le contenu UI avec Compose, en passant le ViewModel à MyApp
        setContent { MyApp(viewModel) }
    }

    override fun onResume() {
        super.onResume()
        // Active le dispatch NFC en premier plan pour que cette activité reçoive les intents NFC
        nfcManager.enableDispatch(this)
    }

    override fun onPause() {
        super.onPause()
        // Désactive le dispatch NFC pour éviter que l'activité reçoive les intents en arrière-plan
        nfcManager.disableDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // Récupère le tag NFC depuis l'intent, en tenant compte de la version Android (API 33+)
        val tag: Tag? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        }

        // Si un tag est détecté, informe le ViewModel pour traitement (lecture, sauvegarde, etc.)
        tag?.let { viewModel.onTagDetected(it) }
    }
}
