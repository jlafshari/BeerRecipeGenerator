// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    extra["kotlinVersion"] = "1.4.31"
    val kotlinVersion: String by extra
    repositories {
        google()
        mavenCentral()
    }
    extra["hiltVersion"] = "2.35"
    val hiltVersion: String by extra
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
