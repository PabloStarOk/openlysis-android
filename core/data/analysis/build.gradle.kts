plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    api(projects.core.common)

    compileOnly(libs.moshi.kotlin)
    compileOnly(libs.google.dagger.hilt.core)

    ksp(libs.moshi.kotlin.codegen)
    ksp(libs.google.dagger.hilt.compiler)
}