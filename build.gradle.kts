// Top-level build file where you can add configuration options common to all sub-projects/modules.
allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ktlint.gradle) apply true
}

dependencies {
    ktlintRuleset(libs.ktlint.ruleset)
}