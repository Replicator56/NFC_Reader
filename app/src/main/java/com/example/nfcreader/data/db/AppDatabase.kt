package com.example.nfcreader.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotation qui définit cette classe comme une base de données Room
// entities : la liste des entités (tables) de la base, ici TagEntity
// version : version de la base, utile pour la migration
// exportSchema : désactivé pour ne pas exporter le schéma au build
@Database(entities = [TagEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Déclare l'accès au DAO TagDao
    abstract fun tagDao(): TagDao

    companion object {
        // Instance volatile pour garantir la visibilité entre threads
        @Volatile private var INSTANCE: AppDatabase? = null

        // Méthode pour obtenir une instance singleton de la base de données
        // Utilise un double-checked locking pour la sécurité thread
        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, // Contexte application pour éviter fuite mémoire
                    AppDatabase::class.java,     // Classe de la base de données
                    "nfc_db"                    // Nom du fichier de base de données
                ).build().also { INSTANCE = it }  // Initialise l'instance après build
            }
    }
}
