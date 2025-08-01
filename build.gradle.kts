plugins {
    id("com.android.application") version "8.9.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.22" apply false
    alias(libs.plugins.compose.compiler) apply false
}
