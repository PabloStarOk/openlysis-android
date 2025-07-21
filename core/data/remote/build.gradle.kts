plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    compileOnly(libs.google.dagger.hilt.core)

    ksp(libs.moshi.kotlin.codegen)
    ksp(libs.google.dagger.hilt.compiler)

    implementation(projects.core.data.analysis.model)
    implementation(projects.core.data.analysis.core)
}