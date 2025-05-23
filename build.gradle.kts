// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.9" apply false
    id("com.google.devtools.ksp") version "2.1.20-1.0.32" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22" apply false
}