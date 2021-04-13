plugins {
    id("java-library")
    id("kotlin")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${project.rootProject.ext["kotlinVersion"]}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.1")
}
