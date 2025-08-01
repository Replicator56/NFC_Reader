package com.example.nfcreader.nfc

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import androidx.activity.ComponentActivity
import java.util.Locale

// Classe utilitaire pour gérer la lecture/écriture NFC et la gestion du dispatch NFC
class NfcManager(private val context: Context) {

    // Obtient l'adaptateur NFC de l'appareil, ou null si non disponible
    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

    // Active la réception des intents NFC sur l'activité fournie,
    // en configurant le foreground dispatch system
    fun enableDispatch(activity: ComponentActivity) {
        nfcAdapter?.enableForegroundDispatch(
            activity,
            PendingIntent.getActivity(
                context, 0,
                Intent(context, activity.javaClass)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), // Evite de lancer plusieurs instances
                PendingIntent.FLAG_MUTABLE // Permet de modifier l'intent si besoin (Android 12+)
            ),
            // Filtre les intents NFC reconnus par l'app
            arrayOf(
                IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
                IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
                IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
            ),
            null // Aucune tech spécifique filtrée ici
        )
    }

    // Désactive la réception NFC en foreground dispatch sur l'activité
    fun disableDispatch(activity: ComponentActivity) {
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    // Lit le contenu NDEF du tag NFC fourni
    // Retourne une chaîne concaténée des payloads texte, ou null si lecture impossible
    fun readTag(tag: Tag): String? {
        val ndef = Ndef.get(tag) ?: return null // Si le tag ne supporte pas NDEF, retourne null
        return try {
            ndef.connect() // Ouvre la connexion avec le tag
            val message = ndef.cachedNdefMessage ?: return null // Lit le message NDEF
            // Concatène les records NDEF en extrayant la payload texte (sans le préfixe)
            message.records.joinToString("\n") {
                String(it.payload.drop(1).toByteArray(), Charsets.UTF_8)
            }
        } catch (e: Exception) {
            null // En cas d'erreur, retourne null
        } finally {
            try { ndef.close() } catch (_: Exception) {} // Ferme la connexion proprement
        }
    }

    // Écrit un message texte sur un tag NFC en mode NDEF
    // Retourne true si l'opération a réussi, false sinon
    fun writeTag(tag: Tag, text: String): Boolean {
        val ndef = Ndef.get(tag) ?: return false // Tag non compatible NDEF
        val record = NdefRecord.createTextRecord(Locale.getDefault().language, text) // Création d'un record texte
        val msg = NdefMessage(arrayOf(record)) // Création du message NDEF complet
        return try {
            ndef.connect() // Connexion au tag
            if (ndef.isWritable) { // Vérifie que le tag est inscriptible
                ndef.writeNdefMessage(msg) // Écrit le message NDEF
                true
            } else false // Tag non inscriptible
        } catch (e: Exception) {
            false // Erreur lors de l'écriture
        } finally {
            try { ndef.close() } catch (_: Exception) {} // Ferme la connexion proprement
        }
    }
}
