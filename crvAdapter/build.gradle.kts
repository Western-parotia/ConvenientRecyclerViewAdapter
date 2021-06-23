import com.buildsrc.kts.AndroidConfig
import com.buildsrc.kts.Dependencies

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(AndroidConfig.compileSdkVersion)

    defaultConfig {

        minSdkVersion(AndroidConfig.minSdkVersion)
        targetSdkVersion(AndroidConfig.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

    }
    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = AndroidConfig.Language.sourceCompatibility
        targetCompatibility = AndroidConfig.Language.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = AndroidConfig.Language.jvmTarget
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
    implementation(Dependencies.Kotlin.kotlin_stdlib)
    implementation(Dependencies.AndroidX.core_ktx)
    api(Dependencies.RecyclerView.chadAdapter)
    implementation(Dependencies.RecyclerView.recyclerView)
}
