plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    //noinspection GradleDependency
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.muflidevs.storyapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.muflidevs.storyapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //glide
    implementation(libs.glide)

    //viewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // coroutine
    implementation(libs.kotlinx.coroutines.android)

    //pagination
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.room.paging)

    //maps
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)

    //room
    implementation(libs.androidx.room.runtime)
    androidTestImplementation(libs.androidx.core.testing)

    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //test
    implementation(libs.androidx.espresso.idling.resource)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.paging.runtime)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.kotlin)
    testRuntimeOnly(libs.junit.jupiter.engine.v582)

    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.espresso.intents)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)

    //android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
}




