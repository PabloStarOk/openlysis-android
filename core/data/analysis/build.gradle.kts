plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.openlysis.data.analysis"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    api(projects.core.common)

    compileOnly(libs.moshi.kotlin)
    compileOnly(libs.google.dagger.hilt.core)

    ksp(libs.moshi.kotlin.codegen)
    ksp(libs.google.dagger.hilt.compiler)
}