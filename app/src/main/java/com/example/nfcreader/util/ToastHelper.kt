package com.example.nfcreader.util

import android.content.Context
import android.widget.Toast

// Objet singleton pour afficher facilement des Toasts dans l'application
object ToastHelper {

    // Fonction utilitaire pour afficher un Toast court avec un message donné
    fun show(context: Context, msg: String) {
        // Création et affichage du Toast avec le contexte et le message fournis
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
