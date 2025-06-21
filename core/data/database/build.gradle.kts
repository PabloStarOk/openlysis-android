plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "com.openlysis.data.local"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("int", "DB_VERSION", "1")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    implementation(projects.core.data.analysis.model)
    implementation(projects.core.data.analysis.core)

    compileOnly(libs.google.dagger.hilt)
    compileOnly(libs.moshi.kotlin)

    ksp(libs.androidx.room.compiler)
    ksp(libs.google.dagger.hilt.compiler)

    coreLibraryDesugaring(libs.android.tools.desugar)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

room {
    schemaDirectory("$projectDir/schemas")
}