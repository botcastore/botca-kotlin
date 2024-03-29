plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("com.google.gms.google-services")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.kevinhomorales.botcakotlin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kevinhomorales.botcakotlin"
        minSdk = 24
        targetSdk = 34
        versionCode = 101
        versionName = "1.0.1"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")

    // FIREBASE
    implementation ("com.google.firebase:firebase-analytics:21.3.0")
    implementation ("com.google.firebase:firebase-auth:22.1.2")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.code.gson:gson:2.9.0")
    implementation ("com.google.firebase:firebase-messaging:23.2.1")
    implementation ("com.google.firebase:firebase-config:21.4.1")
    // FIREBASE

    // PDF
    implementation("com.github.barteksc:android-pdf-viewer:2.8.2")
    // PDF

    // GLIDE
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    // GLIDE

    // CARD VIEW
    implementation ("androidx.cardview:cardview:1.0.0")
    // CARD VIEW

    // RETROFIT
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.")
    implementation ("com.google.code.gson:gson:2.9.0")
    // RETROFIT

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}