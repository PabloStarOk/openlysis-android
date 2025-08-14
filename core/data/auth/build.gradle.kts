plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.datetime)
    api(projects.core.common)
    compileOnly(libs.google.dagger.hilt.core)
    ksp(libs.google.dagger.hilt.compiler)
}