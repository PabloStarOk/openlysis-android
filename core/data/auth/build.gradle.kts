plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.google.devtools.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    compileOnly(libs.google.dagger.hilt.core)
    ksp(libs.google.dagger.hilt.compiler)
}