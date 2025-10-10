plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.openlysis.data.remote"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.rx3)
    implementation(libs.microsoft.signalr)
    implementation(libs.microsoft.signalr.messagepack)
    implementation(libs.jackson.annotations)
    implementation(libs.slf4j.android)

    implementation(projects.core.data.analysis)
    implementation(projects.core.data.auth)
    implementation(projects.core.common)

    coreLibraryDesugaring(libs.android.tools.desugar)

    compileOnly(libs.google.dagger.hilt.core)

    ksp(libs.moshi.kotlin.codegen)
    ksp(libs.google.dagger.hilt.compiler)
}