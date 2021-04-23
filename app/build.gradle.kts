import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    kotlin("android")
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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("debug") {
            resValue("string", "getAllStylesUrl", localProperties["getAllStylesUrl"] as String)
            resValue("string", "generateRecipeUrl", localProperties["generateRecipeUrl"] as String)
            resValue("string", "getAllRecipesUrl", localProperties["getAllRecipesUrl"] as String)
            resValue("string", "recipeBaseUrl", localProperties["recipeBaseUrl"] as String)
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${project.rootProject.ext["kotlinVersion"]}")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("com.google.android.material:material:1.3.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.31")
    implementation("com.android.volley:volley:1.2.0")
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.1")
    implementation(project(":BeerRecipeCore"))
}
