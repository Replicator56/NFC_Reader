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

class NfcManager(private val context: Context) {
    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

    fun enableDispatch(activity: ComponentActivity) {
        nfcAdapter?.enableForegroundDispatch(
            activity,
            PendingIntent.getActivity(
                context, 0,
                Intent(context, activity.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
            ),
            arrayOf(
                IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
                IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
                IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
            ),
            null
        )
    }

    fun disableDispatch(activity: ComponentActivity) {
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    fun readTag(tag: Tag): String? {
        val ndef = Ndef.get(tag) ?: return null
        return try {
            ndef.connect()
            val message = ndef.cachedNdefMessage ?: return null
            message.records.joinToString("\n") {
                String(it.payload.drop(1).toByteArray(), Charsets.UTF_8)
            }
        } catch (e: Exception) {
            null
        } finally {
            try { ndef.close() } catch (_: Exception) {}
        }
    }

    fun writeTag(tag: Tag, text: String): Boolean {
        val ndef = Ndef.get(tag) ?: return false
        val record = NdefRecord.createTextRecord(Locale.getDefault().language, text)
        val msg = NdefMessage(arrayOf(record))
        return try {
            ndef.connect()
            if (ndef.isWritable) {
                ndef.writeNdefMessage(msg)
                true
            } else false
        } catch (e: Exception) {
            false
        } finally {
            try { ndef.close() } catch (_: Exception) {}
        }
    }
}
