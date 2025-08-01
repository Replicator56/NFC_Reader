package com.example.nfcreader.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Définition des styles de texte selon Material Design 3
val Typography = Typography(
    // Style pour le texte principal (bodyLarge)
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,  // Police par défaut du système
        fontWeight = FontWeight.Normal,   // Poids normal (ni gras ni fin)
        fontSize = 16.sp,                 // Taille de la police en scale-independent pixels
        lineHeight = 24.sp,               // Hauteur de ligne pour l'espacement vertical
        letterSpacing = 0.5.sp            // Espacement entre les lettres
    )
    /* D'autres styles par défaut peuvent être personnalisés ici, comme par exemple :
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
