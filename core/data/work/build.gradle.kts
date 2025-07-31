plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.google.dagger.hilt)
}

android {
    namespace = "com.openlysis.data.work"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.workmanager.runtime)
    implementation(libs.androidx.workmanager.runtime.ktx)
    implementation(libs.androidx.hilt.workmanager)
    implementation(libs.google.dagger.hilt)

    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.core.data.analysis.model)
    implementation(projects.core.data.analysis.core)
    implementation(projects.core.notification)

    ksp(libs.google.dagger.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)
}