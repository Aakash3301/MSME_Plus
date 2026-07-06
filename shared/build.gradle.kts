import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import java.io.FileInputStream
import java.io.File

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}
val geminiApiKey = localProperties.getProperty("GEMINI_API_KEY") ?: ""

val generateBuildConfigTask = tasks.register("generateBuildConfig") {
    val apiKey = geminiApiKey
    val outputDir = layout.buildDirectory.dir("generated/source/buildConfig/commonMain/kotlin")
    outputs.dir(outputDir)
    doLast {
        val file = File(outputDir.get().asFile, "com/msme/plus/shared/core/network/BuildEnv.kt")
        file.parentFile.mkdirs()
        file.writeText("""
            package com.msme.plus.shared.core.network
            
            object BuildEnv {
                const val GEMINI_API_KEY = "$apiKey"
            }
        """.trimIndent())
    }
}

kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir(generateBuildConfigTask.map { it.outputs.files.singleFile })
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    androidLibrary {
       namespace = "com.msme.plus.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.cio)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation("com.russhwolf:multiplatform-settings-no-arg:1.1.1")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}