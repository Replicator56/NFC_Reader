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

    private val viewModel: NfcViewModel by viewModels()
    private lateinit var nfcManager: NfcManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nfcManager = NfcManager(this)
        viewModel.nfcManager = nfcManager // injection du manager

        setContent { MyApp(viewModel) }
    }

    override fun onResume() {
        super.onResume()
        nfcManager.enableDispatch(this)
    }

    override fun onPause() {
        super.onPause()
        nfcManager.disableDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val tag: Tag? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        }

        tag?.let { viewModel.onTagDetected(it) }
    }
}
