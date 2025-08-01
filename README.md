# 📱 NFC Reader – Application Android

Application Android qui permet de **lire** et **écrire** des tags NFC, tout en enregistrant l’historique des tags scannés dans une base de données Room.

---

## ✨ Fonctionnalités
- ✅ Lecture d’ID de tag NFC et du contenu NDEF
- ✅ Écriture de texte sur un tag NFC compatible
- ✅ Historique des tags scannés (persisté via Room)
- ✅ Interface utilisateur basée sur **Jetpack Compose**
- ✅ Architecture MVVM avec ViewModel et Repository

---

## 📸 Captures d’écran
TODO()

---

## 🚀 Installation
**Cloner le projet :**
git clone https://github.com/ton-utilisateur/NFCReader.git

---

📂 Structure du projet
app/
 ├── data/
 │   ├── db/               # Base de données Room (TagEntity, TagDao, AppDatabase)
 │   └── repository/       # Repository pour gérer les accès aux données
 ├── nfc/                  # Gestion NFC (NfcManager)
 ├── ui/
 │   ├── main/             # MainActivity et UI Compose
 │   └── viewmodel/        # NfcViewModel (logique métier)
 └── ...

---

🛠️ Technologies utilisées
    Kotlin
    Jetpack Compose
    Room
    Coroutines + StateFlow
    NFC API Android

📋 Prérequis
    Android 8.0 (API 26) ou supérieur
    Appareil compatible NFC
    Autorisation NFC activée

🔥 Fonctionnement
    Approcher un tag NFC de l’appareil
    L’application lit automatiquement l’ID du tag et le contenu NDEF
    Si tu veux écrire sur le tag :
        Saisis un texte → clique sur "Écrire"
    L’historique des tags lus s’affiche dans la liste

📌 Améliorations possibles
    Ajout du support pour d’autres formats NFC (URI, VCard, etc.)
    Exportation de l’historique au format CSV
    Mode sombre personnalisé

🤝 Contribution
Les contributions sont les bienvenues !
Forke le projet, crée une branche et soumets une Pull Request.

📜 Licence
Ce projet est sous licence MIT. Tu peux l’utiliser librement.

👨‍💻 Auteur
Projet développé par Francis LE BORGNE
💬 N’hésite pas à me contacter pour toute question ou suggestion !
