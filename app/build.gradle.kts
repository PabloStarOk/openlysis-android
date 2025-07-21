import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.json.serialization)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.google.dagger.hilt)
}

android {
    namespace = "com.openlysis"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.openlysis"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val localProperties = Properties()
        val localPropertiesFilenames = arrayOf("secret.properties", "settings.properties")
        localPropertiesFilenames.forEach {
            val file = rootProject.file(it)
            if (file.exists() && file.isFile) {
                file.inputStream().use { inputStream ->
                    localProperties.load(inputStream)
                }
            } else {
                throw GradleException(
                    "Required configuration file '$it' not found or is not a file."
                )
            }
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField(
                "String",
                "API_BASE_URL",
                localProperties.getProperty("API_BASE_URL_PROD")
            )

            buildConfigField(
                "Integer",
                "MAX_ATTACHMENT_FILES",
                localProperties.getProperty("MAX_ATTACHMENT_FILES_PROD")
            )

            buildConfigField(
                "Long",
                "MAX_ATTACHMENT_FILE_SIZE_BYTES",
                localProperties.getProperty("MAX_ATTACHMENT_FILE_SIZE_BYTES_PROD")
            )
        }

        debug {
            buildConfigField(
                "String",
                "API_BASE_URL",
                localProperties.getProperty("API_BASE_URL")
            )

            buildConfigField(
                "Integer",
                "MAX_ATTACHMENT_FILES",
                localProperties.getProperty("MAX_ATTACHMENT_FILES")
            )

            buildConfigField(
                "Long",
                "MAX_ATTACHMENT_FILE_SIZE_BYTES",
                localProperties.getProperty("MAX_ATTACHMENT_FILE_SIZE_BYTES")
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
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation)
    implementation(libs.google.dagger.hilt)

    implementation(projects.core.designsystem)
    implementation(projects.feature.tools)
    implementation(projects.feature.results)
    implementation(projects.feature.auth)
    implementation(projects.core.common)
    implementation(projects.core.data.analysis.model)
    implementation(projects.core.data.analysis.core)
    implementation(projects.core.data.auth)
    implementation(projects.core.data.remote)
    implementation(projects.core.data.database)
    implementation(projects.core.data.datastore)

    coreLibraryDesugaring(libs.android.tools.desugar)

    ksp(libs.google.dagger.hilt.compiler)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}