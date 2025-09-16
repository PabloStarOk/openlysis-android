plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.rx3)
    implementation(libs.microsoft.signalr)
    implementation(libs.microsoft.signalr.messagepack)
    implementation(libs.jackson.annotations)

    implementation(projects.core.data.analysis)
    implementation(projects.core.data.auth)
    implementation(projects.core.common)

    compileOnly(libs.google.dagger.hilt.core)

    ksp(libs.moshi.kotlin.codegen)
    ksp(libs.google.dagger.hilt.compiler)
}