import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.playPublisher)
}

val localProperties = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}

/** A release secret, from the environment (CI) or from the untracked local.properties. */
fun secret(name: String): String? = System.getenv(name) ?: localProperties.getProperty(name)

fun git(vararg args: String): String? = providers.exec {
    commandLine("git", *args)
    isIgnoreExitValue = true
}.standardOutput.asText.get().trim().ifEmpty { null }

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}
dependencies {
    implementation(project(":shared"))

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)

    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
}

android {
    namespace = "com.babatunde.okido"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.babatunde.okido"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = git("rev-list", "--count", "HEAD")?.toInt() ?: 1
        versionName = git("describe", "--tags", "--abbrev=0", "--match", "v*")?.removePrefix("v") ?: "0.1.0"
    }
    signingConfigs {
        create("release") {
            secret("OKIDO_KEYSTORE_PATH")?.let { storeFile = rootProject.file(it) }
            storePassword = secret("OKIDO_KEYSTORE_PASSWORD")
            keyAlias = secret("OKIDO_KEY_ALIAS")
            keyPassword = secret("OKIDO_KEY_PASSWORD")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            if (secret("OKIDO_KEYSTORE_PATH") != null) signingConfig = signingConfigs.getByName("release")
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
        compose = true
        buildConfig = true
    }
}

// Uploads with the service account JSON given in ANDROID_PUBLISHER_CREDENTIALS.
play {
    track = "internal"
    defaultToAppBundles = true
}