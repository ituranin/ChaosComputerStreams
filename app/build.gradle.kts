plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.igtuapps.chaoscomputerstreams"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.igtuapps.chaoscomputerstreams"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.leanback)
    implementation(libs.glide)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.androidx.media3.datasource)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.ui.leanback)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
}