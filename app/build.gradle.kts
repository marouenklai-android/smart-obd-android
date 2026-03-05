plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    //alias (libs.plugins.hilt.android)
    //id("org.jetbrains.kotlin.kapt") // REQUIRED for annotation processing
    //id("dagger.hilt.android.plugin")
    //id("org.jetbrains.kotlin.plugin.parcelize")
    //alias(libs.plugins.hilt.android) // <-- add this
    //kotlin("kapt") // <-- required for annotation processing

    //id 'com.google.dagger.hilt.android' version '2.57.1' apply false

}

android {
    namespace = "com.example.obdapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.obdapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    implementation(libs.androidx.runtime)
    implementation(libs.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.ui.graphics)
    implementation(libs.androidx.remote.creation.core)
    implementation(libs.androidx.foundation)
    implementation(libs.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //implementation(libs.obd)
    // Kolin OBD API
    implementation("com.github.eltonvs:kotlin-obd-api:1.3.0")
    implementation(libs.filament.android)



    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")


    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.0")


    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.0")


    implementation ("androidx.core:core-ktx:1.3.2")

    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")


    implementation("com.github.pires:obd-java-api:1.0")
    implementation("com.airbnb.android:lottie-compose:6.3.0")
    //implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    // Hilt
    //implementation ("com.google.dagger:hilt-android:2.48")
    //kapt ("com.google.dagger:hilt-compiler:2.48")

// Hilt ViewModel integration
    //implementation ("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0")
    //kapt ("androidx.hilt:hilt-compiler:1.0.0")
    // Compose + Lifecycle + ViewModel
    //implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Hilt + Compose integration
    //implementation("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")
    //implementation("com.google.dagger:hilt-android:2.48")
    //kapt("com.google.dagger:hilt-compiler:2.48")
    //implementation("com.google.dagger:hilt-android:2.57.1")
    //ksp("com.google.dagger:hilt-android-compiler:2.57.1")

    // Hilt + Compose integration
    //implementation("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")

    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-compiler:2.50")

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // Google Generative AI SDK
    //implementation("com.google.ai:generativelanguage:1.0.0")
}