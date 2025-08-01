package com.example.nfcreader.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Définition du jeu de couleurs pour le mode sombre
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Définition du jeu de couleurs pour le mode clair
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* On peut aussi override d'autres couleurs par défaut comme :
    background, surface, onPrimary, onSecondary, etc.
    */
)

@Composable
fun NFCReaderTheme(
    // Utilise le thème sombre si le système est en mode sombre par défaut
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Active les couleurs dynamiques (Android 12+) par défaut
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Choix du jeu de couleurs selon les paramètres et la version Android
    val colorScheme = when {
        // Si les couleurs dynamiques sont activées et la version Android >= 12 (S)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Sinon, on applique le thème sombre ou clair classique
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Application du thème Material3 avec le schéma de couleurs et la typographie
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // venant généralement d'un fichier Typography.kt
        content = content
    )
}
