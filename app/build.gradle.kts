import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.dagger.hilt.android")
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

android {
    compileSdk = 34
    defaultConfig {
        applicationId = "com.jlafshari.beerrecipegenerator"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("long", "TIMESTAMP", System.currentTimeMillis().toString() + "L")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("debug") {
            buildConfigField("String", "homebrewApiHttpScheme", localProperties["homebrewApiHttpScheme"] as String)
            buildConfigField("String", "homebrewApiBaseDomain", localProperties["homebrewApiBaseDomain"] as String)
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "com.jlafshari.beerrecipegenerator"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${project.rootProject.ext["kotlinVersion"]}")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.11.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.21")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.google.dagger:hilt-android:${project.rootProject.ext["hiltVersion"]}")
    kapt("com.google.dagger:hilt-android-compiler:${project.rootProject.ext["hiltVersion"]}")
    implementation("io.jsonwebtoken:jjwt:0.12.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(project(":BeerRecipeCore"))
    implementation("com.microsoft.identity.client:msal:4.10.0") {
        exclude(group = "io.opentelemetry")
    }
    implementation("io.opentelemetry:opentelemetry-api:1.18.0")
    implementation("io.opentelemetry:opentelemetry-context:1.18.0")
}
