plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    compileOnly(libs.google.dagger.hilt.core)
    ksp(libs.google.dagger.hilt.compiler)
}