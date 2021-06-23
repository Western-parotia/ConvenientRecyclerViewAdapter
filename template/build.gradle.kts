import com.buildsrc.kts.AndroidConfig
import com.buildsrc.kts.Dependencies

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkVersion(AndroidConfig.compileSdkVersion)

    defaultConfig {
        applicationId = "com.kst.template"
        minSdkVersion(AndroidConfig.minSdkVersion)
        targetSdkVersion(AndroidConfig.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("normalSign") {
            storeFile = file("test.jks")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }
    }
    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("normalSign")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("normalSign")
        }
    }
    compileOptions {
        sourceCompatibility = AndroidConfig.Language.sourceCompatibility
        targetCompatibility = AndroidConfig.Language.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = AndroidConfig.Language.jvmTarget
    }
    buildFeatures {
        viewBinding = true
    }
}

configurations.all {
    resolutionStrategy {
        //版本号会改变
        cacheDynamicVersionsFor(10, TimeUnit.SECONDS)
        //版本号不变,1.0-SNAPSHOT 这种应该实时更新
        cacheChangingModulesFor(10, TimeUnit.SECONDS)
    }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(Dependencies.Kotlin.kotlin_stdlib)
    implementation(Dependencies.AndroidX.core_ktx)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.Material.material)
    implementation(Dependencies.AndroidX.constraintlayout)
    implementation(project(":crvAdapter"))
}
