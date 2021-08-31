import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.jlafshari.beerrecipegenerator"
        minSdkVersion(27)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        buildConfigField("long", "TIMESTAMP", System.currentTimeMillis().toString() + "L")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["appAuthRedirectScheme"] = "com.okta.dev-22160898"
    }
    buildTypes {
        getByName("debug") {
            resValue("string", "homebrewApiBaseUrl", localProperties["homebrewApiBaseUrl"] as String)
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${project.rootProject.ext["kotlinVersion"]}")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.31")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.okta.android:oidc-androidx:1.0.17")
    implementation("androidx.annotation:annotation:1.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("com.google.dagger:hilt-android:${project.rootProject.ext["hiltVersion"]}")
    kapt("com.google.dagger:hilt-android-compiler:${project.rootProject.ext["hiltVersion"]}")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.1")
    implementation(project(":BeerRecipeCore"))
}
