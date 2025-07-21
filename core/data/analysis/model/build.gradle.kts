plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    api(projects.core.common)
    compileOnly(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
}