import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermissions
import java.util.Base64
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

// Only release builds read the keychain, so debug builds never trigger a keychain prompt.
val releaseRequested = gradle.startParameter.taskNames.any { it.contains("release", ignoreCase = true) }

// The keychain only exists on macOS: elsewhere (F-Droid's Linux server), the release is built unsigned.
val onMac = System.getProperty("os.name").startsWith("Mac")

/** An entry of the "okido-release" service in the macOS keychain. */
fun keychain(account: String): String? = if (!releaseRequested || !onMac) null else providers.exec {
    commandLine("security", "find-generic-password", "-s", "okido-release", "-a", account, "-w")
    isIgnoreExitValue = true
}.standardOutput.asText.get().trim().ifEmpty { null }

/** A release secret, from the environment (CI), the macOS keychain, or the untracked local.properties. */
fun secret(name: String): String? = System.getenv(name) ?: keychain(name) ?: localProperties.getProperty(name)

/** Where the keystore kept in the keychain is decoded for a release build; deleted once the build is done. */
val keychainKeystore: File = layout.buildDirectory.file("signing/okido-upload.jks").get().asFile

/** Deletes the decoded keystore when the build ends, whether it succeeded or failed. */
abstract class KeystoreCleanup : BuildService<KeystoreCleanup.Params>, AutoCloseable {
    interface Params : BuildServiceParameters {
        val keystore: RegularFileProperty
    }

    override fun close() {
        parameters.keystore.get().asFile.delete()
    }
}

/** The upload keystore: a path from the environment, the keychain's copy (base64), or a path in local.properties. */
fun releaseKeystore(): File? {
    System.getenv("OKIDO_KEYSTORE_PATH")?.let { return rootProject.file(it) }
    keychain("OKIDO_KEYSTORE")?.let { encoded ->
        keychainKeystore.parentFile.mkdirs()
        keychainKeystore.delete()
        // Created readable by the owner only, before a single byte of the key is written.
        Files.createFile(keychainKeystore.toPath(), PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-------")))
        keychainKeystore.writeBytes(Base64.getMimeDecoder().decode(encoded))
        // Starting the service is what gets it closed, so the file is deleted at the end of this build.
        gradle.sharedServices.registerIfAbsent("keystoreCleanup", KeystoreCleanup::class) {
            parameters.keystore = keychainKeystore
        }.get()
        return keychainKeystore
    }
    return localProperties.getProperty("OKIDO_KEYSTORE_PATH")?.let { rootProject.file(it) }
}

val keystore = releaseKeystore()

/** The release version, kept in a file so F-Droid can read it without building. */
val releaseVersion = Properties().apply { rootProject.file("version.properties").inputStream().use { load(it) } }

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
        versionCode = releaseVersion.getProperty("versionCode").toInt()
        versionName = releaseVersion.getProperty("versionName")
    }
    signingConfigs {
        create("release") {
            storeFile = keystore
            storePassword = secret("OKIDO_KEYSTORE_PASSWORD")
            keyAlias = secret("OKIDO_KEY_ALIAS")
            keyPassword = secret("OKIDO_KEY_PASSWORD")
        }
    }
    // Nothing is compressed in the APK: the deflate output depends on the JDK's zlib (macOS here, Debian on
    // F-Droid's server), and the APK must be byte for byte identical on both for F-Droid to publish ours.
    androidResources {
        noCompress += ""
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        dex {
            useLegacyPackaging = false
        }
        jniLibs {
            useLegacyPackaging = false
        }
    }
    buildTypes {
        release {
            if (keystore != null) signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
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

// The keystore is decoded while Gradle configures the build: a cached configuration would skip that step.
tasks.matching { it.name == "packageRelease" || it.name == "signReleaseBundle" }.configureEach {
    notCompatibleWithConfigurationCache("decodes the upload keystore from the keychain at configuration time")
}

// Uploads with the service account JSON given in ANDROID_PUBLISHER_CREDENTIALS.
play {
    track = "internal"
    defaultToAppBundles = true
}
